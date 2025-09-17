package com.divinelink.scenepeek

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.extensions.extractDetailsFromDeepLink
import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import com.divinelink.scenepeek.ui.ThemedActivityDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainViewModel(
  private val createSessionUseCase: CreateSessionUseCase,
  private val findByIdUseCase: FindByIdUseCase,
  private val getJellyseerrProfileUseCase: GetJellyseerrProfileUseCase,
  themedActivityDelegate: ThemedActivityDelegate,
) : ViewModel(),
  ThemedActivityDelegate by themedActivityDelegate {

  private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Completed)
  val uiState: StateFlow<MainUiState> = _uiState

  private val _uiEvent: MutableStateFlow<MainUiEvent> = MutableStateFlow(MainUiEvent.None)
  val uiEvent: StateFlow<MainUiEvent> = _uiEvent

  init {
    refreshJellyseerrSession()
  }

  private fun updateUiEvent(event: MainUiEvent) {
    _uiEvent.value = event
  }

  fun consumeUiEvent() {
    _uiEvent.value = MainUiEvent.None
  }

  fun handleDeepLink(uri: Uri?) {
    if (uri == null) return

    when {
      uri.isForTMDB() -> handleSchemeTMDBRedirect(
        uri = uri,
        onAuthSuccess = {
          viewModelScope.launch {
            createSessionUseCase.invoke(Unit)
          }
        },
      )
      uri.isForIMDB() -> handleSchemeIMDB(
        uri = uri,
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
        val (id, type) = uri.toString().extractDetailsFromDeepLink() ?: return

        when (val mediaType = MediaType.from(type)) {
          MediaType.TV, MediaType.MOVIE -> navigateToMediaDetails(id, mediaType)
          MediaType.PERSON -> navigateToPersonDetails(id)
          MediaType.UNKNOWN -> updateUiEvent(MainUiEvent.None)
        }
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
          gender = it.gender,
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
          gender = Gender.NOT_SET,
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
          mediaType = mediaType,
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
}

private fun handleSchemeIMDB(
  uri: Uri,
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
  uri: Uri,
  onAuthSuccess: () -> Unit,
) {
  if (uri.scheme == "scenepeek" &&
    uri.host == "auth" &&
    uri.path == "/redirect"
  ) {
    onAuthSuccess()
  }
}

private fun Uri.isForTMDB(): Boolean = scheme == "scenepeek" &&
  host == "auth" &&
  path == "/redirect"

private fun Uri.isForIMDB(): Boolean = scheme == "https" &&
  (host == "imdb.com" || host == "www.imdb.com" || host == "m.imdb.com")
