package com.divinelink.feature.discover.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.media.MediaListContent
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.discover.DiscoverAction
import com.divinelink.feature.discover.DiscoverForm
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.R
import com.divinelink.feature.discover.chips.DiscoverFilterChip
import com.divinelink.feature.discover.filters.SelectFilterModalBottomSheet
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun DiscoverContent(
  uiState: DiscoverUiState,
  action: (DiscoverAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
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
    AnimatedVisibility(uiState.isLoading) {
      LinearProgressIndicator(
        modifier = Modifier
          .testTag(TestTags.LINEAR_LOADING_INDICATOR)
          .fillMaxWidth(),
      )
    }

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
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
    ) {
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
          )
          is DiscoverForm.Data -> if (it.isEmpty) {
            BlankSlate(
              modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
              uiState = BlankSlateState.Custom(
                icon = UiDrawable.no_results,
                title = UIText.ResourceText(R.string.feature_discover_empty_result_title),
                description = UIText.ResourceText(
                  R.string.feature_discover_empty_result_description,
                ),
              ),
            )
          } else {
            MediaListContent(
              list = it.media,
              onClick = { media ->
                onNavigate(
                  Navigation.DetailsRoute(
                    mediaType = media.mediaType,
                    id = media.id,
                    isFavorite = media.isFavorite,
                  ),
                )
              },
              onLoadMore = { action(DiscoverAction.LoadMore) },
              onLongClick = { media ->
                onNavigate(Navigation.ActionMenuRoute.Media(media.encodeToString()))
              },
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
    )
  }
}
