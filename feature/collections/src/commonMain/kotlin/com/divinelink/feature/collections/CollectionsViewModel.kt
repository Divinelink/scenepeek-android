package com.divinelink.feature.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsViewModel(
  private val route: Navigation.CollectionRoute,
  private val repository: DetailsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<CollectionsUiState> = MutableStateFlow(
    CollectionsUiState.initial(route),
  )
  val uiState: StateFlow<CollectionsUiState> = _uiState

  init {
    fetchCollectionDetails()
  }

  fun onAction(action: CollectionsAction) {
    when (action) {
      CollectionsAction.Refresh -> fetchCollectionDetails()
    }
  }

  private fun fetchCollectionDetails() {
    _uiState.update { uiState ->
      uiState.copy(
        loading = true,
        error = null,
      )
    }

    viewModelScope.launch {
      repository
        .fetchCollectionDetails(id = uiState.value.id)
        .fold(
          onSuccess = { details ->
            _uiState.update { uiState ->
              uiState.copy(
                loading = false,
                error = null,
                overview = details.overview,
                collectionName = details.collection.name,
                backdropPath = details.collection.backdropPath,
                posterPath = details.collection.posterPath,
                movies = details.movies.sortedWith(
                  compareBy<MediaItem.Media.Movie> { it.releaseDate.isBlank() }
                    .thenBy { it.releaseDate.ifBlank { it.name } },
                ),
              )
            }
          },
          onFailure = {
            _uiState.update { uiState ->
              uiState.copy(
                loading = false,
                error = BlankSlateState.Contact,
              )
            }
          },
        )
    }
  }
}
