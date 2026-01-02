package com.divinelink.feature.search.ui

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.ui.blankslate.BlankSlateState

data class SearchUiState(
  val query: String,
  val isLoading: Boolean,
  val focusSearch: Boolean,
  val tabs: List<SearchTab>,
  val selectedTabIndex: Int,
  val pages: Map<SearchTab, Int>,
  val forms: Map<SearchTab, SearchForm<MediaItem.Media>>,
  val canFetchMore: Map<SearchTab, Boolean>,
) {
  companion object {
    fun initial() = SearchUiState(
      query = "",
      isLoading = false,
      focusSearch = false,
      tabs = SearchTab.entries,
      selectedTabIndex = 0,
      pages = SearchTab.entries.associateWith { tab ->
        when (tab) {
          SearchTab.All -> 1
          SearchTab.Movie -> 1
          SearchTab.People -> 1
          SearchTab.TV -> 1
        }
      },
      forms = SearchTab.entries.associateWith { tab ->
        when (tab) {
          SearchTab.All -> SearchForm.Initial
          SearchTab.Movie -> SearchForm.Initial
          SearchTab.People -> SearchForm.Initial
          SearchTab.TV -> SearchForm.Initial
        }
      },
      canFetchMore = SearchTab.entries.associateWith { tab ->
        when (tab) {
          SearchTab.All -> true
          SearchTab.Movie -> true
          SearchTab.People -> true
          SearchTab.TV -> true
        }
      },
    )
  }

  val selectedTab = tabs[selectedTabIndex]
  val selectedPage = pages[selectedTab]
}

sealed interface SearchForm<out T : MediaItem.Media> {
  data object Initial : SearchForm<Nothing>
  data object Loading : SearchForm<Nothing>

  sealed class Error(val blankSlate: BlankSlateState) : SearchForm<Nothing> {
    data object Network : Error(BlankSlateState.Offline)
    data object Unknown : Error(BlankSlateState.Generic)
  }

  data class Data<T : MediaItem.Media>(
    val tab: SearchTab,
    val paginationData: Map<Int, List<MediaItem>>,
  ) : SearchForm<T> {
    val media = paginationData.values.flatten().distinctBy { it.id }
    val isEmpty: Boolean = media.isEmpty()
  }
}
