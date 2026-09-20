package com.divinelink.feature.details.media.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.details.media.DetailSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.ui.blankslate.toBlankSlateState
import com.divinelink.feature.details.media.ui.section.DetailSectionData
import com.divinelink.feature.details.media.ui.section.DetailSectionUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DetailsSectionViewModel(
  section: DetailSection,
  private val repository: DetailsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DetailSectionUiState> = MutableStateFlow(
    DetailSectionUiState.initial(section),
  )
  val uiState: StateFlow<DetailSectionUiState> = _uiState

  init {
    when (section) {
      is DetailSection.MovieRecommendations -> fetchMovieRecommendations(section.id)
      is DetailSection.TvRecommendation -> fetchTvRecommendations(section.id)
      is DetailSection.MovieReviews -> fetchReviews(MediaType.MOVIE)
      is DetailSection.TvReviews -> fetchReviews(MediaType.TV)
    }
  }

  private fun fetchReviews(type: MediaType) {
    val id = uiState.value.section.media.mediaId
    val request = when (type) {
      MediaType.TV -> MediaRequestApi.TV(seriesId = id)
      MediaType.MOVIE -> MediaRequestApi.Movie(movieId = id)
      else -> return
    }

    repository
      .fetchMediaReviews(request)
      .onEach { response ->
        response.fold(
          onSuccess = { reviews ->
            _uiState.update { state ->
              state.copy(data = DetailSectionData.Reviews(reviews))
            }
          },
          onFailure = { error ->
            _uiState.update { state ->
              val data = when (val currentData = state.data) {
                is DetailSectionData.Error -> DetailSectionData.Error(error.toBlankSlateState())
                DetailSectionData.Loading -> DetailSectionData.Error(error.toBlankSlateState())
                is DetailSectionData.Media -> DetailSectionData.Error(error.toBlankSlateState())
                is DetailSectionData.Reviews -> currentData
              }
              state.copy(data = data)
            }
          },
        )
      }
      .launchIn(viewModelScope)
  }

  private fun <T> fetchRecommendations(
    id: Long,
    fetchRequest: (Long) -> Flow<Result<T>>,
    transformResponse: (T) -> List<MediaItem.Media>,
  ) = fetchRequest(id)
    .distinctUntilChanged()
    .catch { error ->
      _uiState.update { state ->
        val data = when (state.data) {
          is DetailSectionData.Error -> DetailSectionData.Error(error.toBlankSlateState())
          DetailSectionData.Loading -> DetailSectionData.Error(error.toBlankSlateState())
          is DetailSectionData.Reviews -> DetailSectionData.Error(error.toBlankSlateState())
          is DetailSectionData.Media -> state.data
        }
        state.copy(data = data)
      }
    }
    .onEach { result ->
      result.onSuccess { response ->
        _uiState.update { state ->
          val data = (state.data as? DetailSectionData.Media?)?.paginationData ?: mapOf(
            1 to transformResponse(response),
          )
          state.copy(data = DetailSectionData.Media(data))
        }
      }
    }
    .launchIn(viewModelScope)

  private fun fetchTvRecommendations(id: Long) = fetchRecommendations(
    id = id,
    fetchRequest = { repository.fetchRecommendedTv(MediaRequestApi.TV(it)) },
    transformResponse = { response -> response.list },
  )

  private fun fetchMovieRecommendations(id: Long) = fetchRecommendations(
    id = id,
    fetchRequest = { repository.fetchRecommendedMovies(MediaRequestApi.Movie(it)) },
    transformResponse = { response -> response.list },
  )
}
