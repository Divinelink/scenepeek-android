package com.divinelink.scenepeek.home.ui

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.Filter
import com.divinelink.core.ui.no_results
import com.divinelink.scenepeek.Res
import com.divinelink.scenepeek.home__empty_filtered_result_description
import com.divinelink.scenepeek.home__empty_filtered_result_title

@Immutable
data class HomeViewState(
  val isLoading: Boolean,
  val filters: List<Filter>,
  val popularMovies: MediaSection,
  val filteredResults: MediaSection?,
  val error: BlankSlateState?,
  val mode: HomeMode,
  val pages: Map<HomePage, Int>,
  val retryAction: HomeMode?,
) {
  val initialLoading = isLoading && popularMovies.data.isEmpty()

  val showFavorites = filters.find { it.name == HomeFilter.Liked.filter.name }?.isSelected

  val isEmpty: Boolean = when (mode) {
    HomeMode.Browser -> popularMovies.data.isEmpty()
    HomeMode.Filtered -> filteredResults?.data?.isEmpty() ?: true
  }

  private val emptyContentUiState: BlankSlateState? = when (mode) {
    HomeMode.Filtered -> BlankSlateState.Custom(
      icon = UiDrawable.no_results,
      title = UIText.ResourceText(Res.string.home__empty_filtered_result_title),
      description = UIText.ResourceText(Res.string.home__empty_filtered_result_description),
    )
    else -> null
  }

  val blankSlate = if (mode == HomeMode.Filtered && isEmpty) {
    emptyContentUiState
  } else if (isEmpty && error == null) {
    emptyContentUiState
  } else {
    error
  }

  companion object {
    fun initial() = HomeViewState(
      isLoading = true,
      popularMovies = MediaSection(
        data = emptyList(),
        shouldLoadMore = true,
      ),
      filters = HomeFilter.entries.map { it.filter },
      filteredResults = null,
      error = null,
      mode = HomeMode.Browser,
      pages = mapOf(
        HomePage.Popular to 1,
      ),
      retryAction = null,
    )
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
