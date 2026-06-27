package com.divinelink.feature.awards.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.awards.AwardsRepository
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.blankslate.toBlankSlateState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AwardCategoryViewModel(
  route: Navigation.AwardCategoryRoute,
  private val awardsRepository: AwardsRepository,
  private val detailsRepository: DetailsRepository,
  private val personRepository: PersonRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<AwardCategoryUiState> = MutableStateFlow(
    AwardCategoryUiState.initial(
      ceremonyId = route.ceremonyId,
      category = route.category,
    ),
  )
  val uiState: StateFlow<AwardCategoryUiState> = _uiState

  init {
    fetchAwards()
  }

  fun onAction(action: AwardCategoryAction) {
    when (action) {
      AwardCategoryAction.OnRetry -> fetchAwards()
      is AwardCategoryAction.FetchMediaItem -> fetchMediaItem(action.nominee)
    }
  }

  private fun fetchMediaItem(item: LoadingUiItem<AwardNominee>) {
    if (item.mediaState !is ItemState.Data) {
      when (item.item.media.mediaType) {
        MediaType.TV,
        MediaType.MOVIE,
          -> fetchMediaDetails(item)
        MediaType.PERSON -> fetchPersonDetails(item)
        MediaType.UNKNOWN -> Unit
      }
    }
  }

  private fun fetchMediaDetails(item: LoadingUiItem<AwardNominee>) {
    detailsRepository
      .fetchMediaItem(item.item.media)
      .distinctUntilChanged()
      .catch { Napier.e { it.message.toString() } }
      .onEach { result ->
        updateMediaItem(
          item = item,
          result = result,
        )
      }
      .launchIn(viewModelScope)
  }

  private fun fetchPersonDetails(item: LoadingUiItem<AwardNominee>) {
    personRepository
      .fetchPersonDetails(item.item.media.mediaId)
      .distinctUntilChanged()
      .catch { Napier.e { it.message.toString() } }
      .onEach { result ->
        updatePersonItem(
          item = item,
          result = result,
        )
      }
      .launchIn(viewModelScope)
  }

  private fun fetchAwards() {
    viewModelScope.launch {
      _uiState.update { state ->
        state.copy(
          loading = true,
          error = null,
        )
      }

      awardsRepository
        .fetchAwards(
          ceremonyId = uiState.value.ceremonyId,
          categoryId = uiState.value.category.id,
        )
        .fold(
          onSuccess = { result ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = null,
                awards = result.associate { award ->
                  award.year to award.nominees.map { nominee ->
                    LoadingUiItem(
                      item = nominee,
                      mediaState = ItemState.Loading,
                    )
                  }
                },
              )
            }
          },
          onFailure = { error ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = error.toBlankSlateState(),
              )
            }
          },
        )
    }
  }

  private fun updateMediaItem(
    item: LoadingUiItem<AwardNominee>,
    result: Resource<MediaItem.Media?>,
  ) {
    _uiState.update { uiState ->
      val newAwards = uiState.awards.mapValues { (_, itemList) ->
        itemList.map { uiItem ->
          if (uiItem.item.media.mediaId == item.item.media.mediaId) {
            when (result) {
              is Resource.Error -> uiItem.copy(
                mediaState = null,
              )
              is Resource.Loading -> uiItem.copy(
                mediaState = result.data?.let { data ->
                  ItemState.Data(
                    item = data,
                    loading = false,
                  )
                } ?: ItemState.Loading,
              )
              is Resource.Success -> uiItem.copy(
                mediaState = result.data?.let { data ->
                  ItemState.Data(
                    item = data,
                    loading = false,
                  )
                },
              )
            }
          } else {
            uiItem
          }
        }
      }
      uiState.copy(awards = newAwards)
    }
  }

  private fun updatePersonItem(
    item: LoadingUiItem<AwardNominee>,
    result: Result<PersonDetails>,
  ) {
    _uiState.update { uiState ->
      val newAwards = uiState.awards.mapValues { (_, itemList) ->
        itemList.map { uiItem ->
          if (uiItem.item.media.mediaId == item.item.media.mediaId) {
            result.fold(
              onSuccess = { details ->
                uiItem.copy(
                  mediaState = ItemState.Data(
                    item = details.person,
                    loading = false,
                  ),
                )
              },
              onFailure = { uiItem.copy(mediaState = null) },
            )
          } else {
            uiItem
          }
        }
      }
      uiState.copy(awards = newAwards)
    }
  }
}
