package com.divinelink.scenepeek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.DeepLinkUri
import com.divinelink.core.commons.extensions.extractDetailsFromScenePeekDeepLink
import com.divinelink.core.commons.extensions.extractDetailsFromTMDBDeepLink
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.network.Resource
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
  private val createSessionUseCase: CreateSessionUseCase,
  private val findByIdUseCase: FindByIdUseCase,
  private val getJellyseerrProfileUseCase: GetJellyseerrProfileUseCase,
  val networkMonitor: NetworkMonitor,
  val onboardingManager: OnboardingManager,
  val preferencesRepository: PreferencesRepository,
  val navigationProviders: List<NavGraphExtension>,
  val appInfoRepository: AppInfoRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Completed)
  val uiState: StateFlow<MainUiState> = _uiState

  private val _uiEvent: MutableStateFlow<MainUiEvent> = MutableStateFlow(MainUiEvent.None)
  val uiEvent: StateFlow<MainUiEvent> = _uiEvent

  init {
    refreshJellyseerrSession()

    appInfoRepository
      .fetchLatestAppVersion()
      .distinctUntilChanged()
      .onEach { result ->
        when (result) {
          is Resource.Error -> Napier.d { result.toString() }
          is Resource.Loading -> Napier.d { result.toString() }
          is Resource.Success -> Napier.d { result.toString() }
        }
      }
      .launchIn(viewModelScope)
  }

  private fun updateUiEvent(event: MainUiEvent) {
    _uiEvent.value = event
  }

  fun consumeUiEvent() {
    _uiEvent.value = MainUiEvent.None
  }

  fun handleDeepLink(uri: String?) {
    val deeplinkUri = DeepLinkUri.parse(uri) ?: return

    when {
      deeplinkUri.isForTMDBAuth() -> handleSchemeTMDBRedirect(
        uri = deeplinkUri,
        onAuthSuccess = {
          viewModelScope.launch {
            createSessionUseCase.invoke(Unit)
          }
        },
      )
      deeplinkUri.isDeeplinkFromScenePeek() -> handleScenePeekDeeplink(deeplinkUri)
      deeplinkUri.isForIMDB() -> handleSchemeIMDB(
        uri = deeplinkUri,
        onHandleDeeplink = { imdbId ->
          viewModelScope.launch {
            findByIdUseCase
              .invoke(imdbId)
              .first()
              .fold(
                onSuccess = { mediaItem ->
                  when (mediaItem) {
                    is MediaItem.Media.Movie,
                    is MediaItem.Media.TV,
                      -> navigateToMediaDetails(mediaItem.id, mediaItem.mediaType)
                    is MediaItem.Person -> navigateToPersonDetails(mediaItem)
                    MediaItem.Unknown -> updateUiEvent(MainUiEvent.None)
                  }
                },
                onFailure = { updateUiEvent(MainUiEvent.None) },
              )
          }
        },
      )
      else -> {
        val (id, type) = deeplinkUri.raw.extractDetailsFromTMDBDeepLink() ?: return

        handleNavigation(
          mediaId = id,
          mediaType = MediaType.from(type),
        )
      }
    }
  }

  private fun navigateToPersonDetails(it: MediaItem.Person) {
    updateUiEvent(
      MainUiEvent.NavigateToPersonDetails(
        PersonRoute(
          id = it.id.toLong(),
          knownForDepartment = it.knownForDepartment,
          name = it.name,
          profilePath = it.profilePath,
          gender = it.gender.value,
        ),
      ),
    )
  }

  private fun navigateToPersonDetails(id: Int) {
    updateUiEvent(
      MainUiEvent.NavigateToPersonDetails(
        PersonRoute(
          id = id.toLong(),
          knownForDepartment = null,
          name = null,
          profilePath = null,
          gender = Gender.NOT_SET.value,
        ),
      ),
    )
  }

  private fun navigateToMediaDetails(
    id: Int,
    mediaType: MediaType,
  ) {
    updateUiEvent(
      MainUiEvent.NavigateToDetails(
        DetailsRoute(
          id = id,
          mediaType = mediaType.value,
          isFavorite = false,
        ),
      ),
    )
  }

  private fun refreshJellyseerrSession() {
    getJellyseerrProfileUseCase
      .invoke(true)
      .launchIn(viewModelScope)
  }

  private fun handleScenePeekDeeplink(uri: DeepLinkUri) {
    val (id, type) = uri.raw.extractDetailsFromScenePeekDeepLink() ?: return

    handleNavigation(
      mediaId = id,
      mediaType = MediaType.from(type),
    )
  }

  private fun handleNavigation(
    mediaId: Int,
    mediaType: MediaType,
  ) {
    when (mediaType) {
      MediaType.TV, MediaType.MOVIE -> navigateToMediaDetails(mediaId, mediaType)
      MediaType.PERSON -> navigateToPersonDetails(mediaId)
      MediaType.UNKNOWN -> updateUiEvent(MainUiEvent.None)
    }
  }
}

private fun handleSchemeIMDB(
  uri: DeepLinkUri,
  onHandleDeeplink: (String) -> Unit,
) {
  val path = uri.path ?: return

  when {
    path.startsWith("/title/") -> {
      val imdbId = path.substringAfter("/title/").substringBefore("/")
      if (imdbId.startsWith("tt")) {
        onHandleDeeplink(imdbId)
      }
    }

    path.startsWith("/name/") -> {
      val nameId = path.substringAfter("/name/").substringBefore("/")
      if (nameId.startsWith("nm")) {
        onHandleDeeplink(nameId)
      }
    }
  }
}

private fun handleSchemeTMDBRedirect(
  uri: DeepLinkUri,
  onAuthSuccess: () -> Unit,
) {
  if (
    uri.scheme == "scenepeek" &&
    uri.host == "auth" &&
    uri.path == "/redirect"
  ) {
    onAuthSuccess()
  }
}

private fun DeepLinkUri.isForTMDBAuth(): Boolean = scheme == "scenepeek" &&
  host == "auth" &&
  path == "/redirect"

private fun DeepLinkUri.isDeeplinkFromScenePeek(): Boolean = scheme == "scenepeek" &&
  (host == "movie" || host == "tv")

private fun DeepLinkUri.isForIMDB(): Boolean = scheme == "https" &&
  (host == "imdb.com" || host == "www.imdb.com" || host == "m.imdb.com")
