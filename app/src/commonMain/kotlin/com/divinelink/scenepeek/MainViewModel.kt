package com.divinelink.scenepeek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.DeepLinkUri
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.model.deeplink.DeeplinkPath
import com.divinelink.core.model.deeplink.extractRouteFromDeeplink
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
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

    appInfoRepository.updaterOptIn
      .distinctUntilChanged()
      .onEach { enabled ->
        appInfoRepository
          .fetchLatestAppVersion(fetchRemote = enabled)
          .launchIn(viewModelScope)
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
                    is MediaItem.Media.Movie -> navigate(DeeplinkPath.Movie(mediaItem.id.toLong()))
                    is MediaItem.Media.TV -> navigate(DeeplinkPath.TV(mediaItem.id.toLong()))
                    is MediaItem.Person -> navigate(
                      DeeplinkPath.Person(
                        id = mediaItem.id.toLong(),
                        knownForDepartment = mediaItem.knownForDepartment,
                        name = mediaItem.name,
                        profilePath = mediaItem.profilePath,
                        gender = mediaItem.gender.value,
                      ),
                    )
                    MediaItem.Unknown -> updateUiEvent(MainUiEvent.None)
                  }
                },
                onFailure = { updateUiEvent(MainUiEvent.None) },
              )
          }
        },
      )
      else -> handleScenePeekDeeplink(deeplinkUri)
    }
  }

  private fun refreshJellyseerrSession() {
    getJellyseerrProfileUseCase
      .invoke(true)
      .launchIn(viewModelScope)
  }

  private fun handleScenePeekDeeplink(uri: DeepLinkUri) {
    val route = uri.raw.extractRouteFromDeeplink() ?: return

    navigate(route)
  }

  private fun navigate(deeplink: DeeplinkPath) {
    val route = when (deeplink) {
      is DeeplinkPath.Collection -> Navigation.CollectionRoute(
        id = deeplink.id.toInt(),
        name = "",
        backdropPath = null,
        posterPath = "",
      )
      is DeeplinkPath.Episode -> Navigation.EpisodeRoute(
        showId = deeplink.showId.toInt(),
        showTitle = "",
        seasonTitle = "",
        seasonNumber = deeplink.seasonNumber,
        episodeIndex = deeplink.episodeNumber,
      )
      is DeeplinkPath.List -> Navigation.ListDetailsRoute(
        id = deeplink.id.toInt(),
        name = "",
        backdropPath = null,
        description = "",
        public = false,
      )
      is DeeplinkPath.Person -> PersonRoute(
        id = deeplink.id,
        knownForDepartment = deeplink.knownForDepartment,
        name = deeplink.name,
        profilePath = deeplink.profilePath,
        gender = deeplink.gender ?: Gender.NOT_SET.value,
      )
      is DeeplinkPath.Season -> Navigation.SeasonRoute(
        showId = deeplink.showId.toInt(),
        seasonNumber = deeplink.seasonNumber,
        backdropPath = null,
        title = "",
      )
      is DeeplinkPath.Movie -> DetailsRoute(
        id = deeplink.id.toInt(),
        mediaType = MediaType.MOVIE.value,
        isFavorite = false,
      )
      is DeeplinkPath.TV -> DetailsRoute(
        id = deeplink.id.toInt(),
        mediaType = MediaType.TV.value,
        isFavorite = false,
      )
    }

    updateUiEvent(MainUiEvent.Navigate(route))
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
  (host == "movie" || host == "tv" || host == "person" || host == "collection" || host == "list")

private fun DeepLinkUri.isForIMDB(): Boolean = scheme == "https" &&
  (host == "imdb.com" || host == "www.imdb.com" || host == "m.imdb.com")
