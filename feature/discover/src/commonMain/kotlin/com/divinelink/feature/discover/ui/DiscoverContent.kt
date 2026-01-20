package com.divinelink.feature.discover.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.clearButton
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.core.ui.resources.no_results
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.discover.DiscoverAction
import com.divinelink.feature.discover.DiscoverForm
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.chips.DiscoverFilterChip
import com.divinelink.feature.discover.filters.SelectFilterModalBottomSheet
import com.divinelink.feature.discover.resources.Res
import com.divinelink.feature.discover.resources.feature_discover_empty_result_description
import com.divinelink.feature.discover.resources.feature_discover_empty_result_title
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun DiscoverContent(
  uiState: DiscoverUiState,
  action: (DiscoverAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )
  var filterModal by remember { mutableStateOf<FilterModal?>(null) }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      action.invoke(DiscoverAction.OnSelectTab(page))
    }
  }

  filterModal?.let { type ->
    SelectFilterModalBottomSheet(
      type = type,
      mediaType = uiState.selectedTab.mediaType,
      onDismissRequest = {
        filterModal = null
        action(DiscoverAction.DiscoverMedia)
      },
    )
  }

  Column {
    ScenePeekSecondaryTabs(
      tabs = uiState.tabs,
      selectedIndex = uiState.selectedTabIndex,
      onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
    )

    LazyRow(
      modifier = Modifier
        .animateContentSize()
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
      contentPadding = PaddingValues(
        start = MaterialTheme.dimensions.keyline_8,
        end = MaterialTheme.dimensions.keyline_16,
      ),
    ) {
      clearButton(
        isVisible = uiState.currentFilters.hasSelectedFilters,
        onClearClick = { action(DiscoverAction.ClearFilters) },
      )

      item {
        DiscoverFilterChip.Genre(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filters = uiState.currentFilters.genres,
          onClick = { filterModal = FilterModal.Genre },
        )
      }

      item {
        DiscoverFilterChip.Year(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filter = uiState.currentFilters.year,
          onClick = { filterModal = FilterModal.Year },
        )
      }

      item {
        DiscoverFilterChip.Language(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          language = uiState.currentFilters.language,
          onClick = { filterModal = FilterModal.Language },
        )
      }

      item {
        DiscoverFilterChip.Country(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          country = uiState.currentFilters.country,
          onClick = { filterModal = FilterModal.Country },
        )
      }

      item {
        DiscoverFilterChip.VoteAverage(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          votes = uiState.currentFilters.votes,
          voteAverage = uiState.currentFilters.voteAverage,
          onClick = { filterModal = FilterModal.VoteAverage },
        )
      }
    }

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
    ) { page ->
      uiState.forms.values.elementAt(page).let {
        when (it) {
          is DiscoverForm.Initial -> DiscoverInitialContent(tab = uiState.selectedTab)
          is DiscoverForm.Loading -> LoadingContent()
          is DiscoverForm.Error -> BlankSlate(
            modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
            uiState = it.blankSlate,
            onRetry = { action(DiscoverAction.DiscoverMedia) },
          )
          is DiscoverForm.Data -> if (it.isEmpty) {
            BlankSlate(
              modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
              uiState = BlankSlateState.Custom(
                icon = UiDrawable.no_results,
                title = UIText.ResourceText(Res.string.feature_discover_empty_result_title),
                description = UIText.ResourceText(
                  Res.string.feature_discover_empty_result_description,
                ),
              ),
            )
          } else {
            ScrollableMediaContent(
              items = it.media,
              section = ViewableSection.DISCOVER,
              onLoadMore = { action(DiscoverAction.LoadMore) },
              onSwitchViewMode = onSwitchViewMode,
              onClick = { media -> media.toRoute()?.let { route -> onNavigate(route) } },
              onLongClick = { media ->
                onNavigate(Navigation.ActionMenuRoute.Media(media.encodeToString()))
              },
              canLoadMore = uiState.canFetchMoreForSelectedTab,
            )
          }
        }
      }
    }
  }
}

@Composable
@Previews
fun DiscoverContentPreview(
  @PreviewParameter(DiscoverUiStateParameterProvider::class) state: DiscoverUiState,
) {
  PreviewLocalProvider {
    DiscoverContent(
      uiState = state,
      action = {},
      onNavigate = {},
      onSwitchViewMode = {},
    )
  }
}
