package com.divinelink.feature.home.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.extensions.epochSecondsToLocalDate
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.model.entity.EntityChange
import com.divinelink.core.model.track.WatchedEpisode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class WatchHistoryViewModel(
  private val trackRepository: TrackRepository,
  private val clockProvider: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<WatchHistoryUiState> = MutableStateFlow(
    WatchHistoryUiState.initial,
  )
  val uiState: StateFlow<WatchHistoryUiState> = _uiState

  init {
    trackRepository
      .observeRecentHistory()
      .distinctUntilChanged()
      .onEach { history ->
        val lastId = history.lastOrNull()?.watchedId
        val lastWatchedAt = history.lastOrNull()?.watchedAt
        val groupedHistory = history.groupBy { item ->
          item.watchedAt.epochSecondsToLocalDate(clockProvider.timezone)
        }

        _uiState.update { state ->
          state.copy(
            history = mergeWatchHistory(
              current = groupedHistory,
              new = state.history,
            ),
            loading = false,
            initialFetch = false,
            lastId = if (state.initialFetch) {
              lastId
            } else {
              state.lastId
            },
            lastWatchedAt = if (state.initialFetch) {
              lastWatchedAt
            } else {
              state.lastWatchedAt
            },
          )
        }
      }
      .launchIn(viewModelScope)

    trackRepository
      .historyEvents
      .distinctUntilChanged()
      .onEach { event ->
        when (event) {
          is EntityChange.Added -> Unit
          is EntityChange.Removed -> _uiState.update { state ->
            state.copy(
              history = state.history.removeIds(event.ids),
            )
          }
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: WatchHistoryAction) {
    when (action) {
      WatchHistoryAction.LoadMore -> handleLoadMore()
    }
  }

  private fun handleLoadMore() {
    val state = _uiState.value

    if (state.loading || !state.canLoadMore) return

    viewModelScope.launch {
      _uiState.update { it.copy(loading = true) }
      val id = state.lastId ?: Long.MAX_VALUE

      val newItems = trackRepository.getWatchHistory(id, state.lastWatchedAt)
      if (newItems.isEmpty()) {
        _uiState.update {
          it.copy(
            loading = false,
            canLoadMore = false,
          )
        }
      } else {
        _uiState.update { state ->
          state.copy(
            history = mergeWatchHistory(
              current = state.history,
              new = newItems.groupBy {
                it.watchedAt.epochSecondsToLocalDate(clockProvider.timezone)
              },
            ),
            lastWatchedAt = newItems.last().watchedAt,
            lastId = newItems.last().watchedId,
            loading = false,
          )
        }
      }
    }
  }

  private fun mergeWatchHistory(
    current: Map<LocalDate, List<WatchedEpisode>>,
    new: Map<LocalDate, List<WatchedEpisode>>,
  ): Map<LocalDate, List<WatchedEpisode>> {
    return (current.entries + new.entries)
      .groupBy({ it.key }, { it.value })
      .mapValues { (_, listOfLists) ->
        listOfLists.flatten().distinctBy { it.episode.id }
      }
  }

  fun Map<LocalDate, List<WatchedEpisode>>.removeIds(
    ids: Set<Long>,
  ): Map<LocalDate, List<WatchedEpisode>> =
    mapValues { (_, items) ->
      items
        .filterNot { it.episode.id.toLong() in ids }
        .distinctBy { it.watchedId }
    }.filterValues { it.isNotEmpty() }
}
