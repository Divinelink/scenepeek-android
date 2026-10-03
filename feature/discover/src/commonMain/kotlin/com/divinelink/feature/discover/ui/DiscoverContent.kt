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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.discover.DiscoverFilterType
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.clearButton
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.core.ui.resources.core_ui_content_rating
import com.divinelink.core.ui.resources.core_ui_genres
import com.divinelink.core.ui.resources.core_ui_information_companies
import com.divinelink.core.ui.resources.core_ui_keywords
import com.divinelink.core.ui.resources.no_results
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.discover.DiscoverAction
import com.divinelink.feature.discover.DiscoverForm
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.DiscoverViewModel
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.chips.DiscoverFilterChip
import com.divinelink.feature.discover.filters.SelectFilterModalBottomSheet
import com.divinelink.feature.discover.resources.Res
import com.divinelink.feature.discover.resources.feature_discover_empty_result_description
import com.divinelink.feature.discover.resources.feature_discover_empty_result_title
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DiscoverSectionsContent(
  uiState: DiscoverSectionsUiState,
  route: Navigation.Discover,
  onSelectTab: (Int) -> Unit,
  onNavigate: (Navigation) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      onSelectTab(page)
    }
  }

  Column {
    ScenePeekSecondaryTabs(
      tabs = uiState.tabs,
      selectedIndex = uiState.selectedTabIndex,
      onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
    )

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
    ) { page ->
      uiState.tabs[page].let { tab ->
        when (tab) {
          MediaTab.Movie -> DiscoverContent(
            mediaType = MediaType.MOVIE,
            route = route,
            onNavigate = onNavigate,
            onSwitchPreferences = onSwitchPreferences,
          )
          MediaTab.TV -> DiscoverContent(
            mediaType = MediaType.TV,
            route = route,
            onNavigate = onNavigate,
            onSwitchPreferences = onSwitchPreferences,
          )
        }
      }
    }
  }
}

@Composable
@Previews
fun DiscoverSectionsContentPreview(
  @PreviewParameter(DiscoverUiStateParameterProvider::class) state: DiscoverUiState,
) {
  PreviewLocalProvider {
//    DiscoverSectionsContent(
//      uiState = state,
//      action = {},
//      onNavigate = {},
//      onSwitchPreferences = {},
//    )
  }
}

@Composable
private fun DiscoverContent(
  mediaType: MediaType,
  route: Navigation.Discover,
  viewModel: DiscoverViewModel = koinViewModel(
    key = "$route:${mediaType.value}",
  ) { parametersOf(mediaType, route) },
  onNavigate: (Navigation) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var filterModal by remember { mutableStateOf<FilterModal?>(null) }

  val filterState = rememberLazyListState()
  LaunchedEffect(Unit) {
    filterState.scrollToItem(uiState.filters.firstSelectedFilterIndex)
  }

  filterModal?.let { type ->
    SelectFilterModalBottomSheet(
      type = type,
      uuid = uiState.uuid,
      mediaType = mediaType,
      onDismissRequest = {
        filterModal = null
        viewModel.onAction(DiscoverAction.DiscoverMedia)
      },
    )
  }

  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
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
      state = filterState,
    ) {
      clearButton(
        isVisible = uiState.filters.hasSelectedFilters,
        onClearClick = { viewModel.onAction(DiscoverAction.ClearFilters) },
      )

      item {
        DiscoverFilterChip.MultiSelect(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filters = uiState.filters.genres,
          onClick = { filterModal = FilterModal.Genre },
          title = UIText.ResourceText(UiString.core_ui_genres),
          name = uiState.filters.genres.firstOrNull()?.name,
        )
      }

      if (uiState.availableFilters.contains(DiscoverFilterType.Year)) {
        item {
          DiscoverFilterChip.Year(
            modifier = Modifier
              .animateItem()
              .animateContentSize(),
            filter = uiState.filters.year,
            onClick = { filterModal = FilterModal.Year },
          )
        }
      }

      item {
        DiscoverFilterChip.Language(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          language = uiState.filters.language,
          onClick = { filterModal = FilterModal.Language },
        )
      }

      item {
        DiscoverFilterChip.Country(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          country = uiState.filters.country,
          onClick = { filterModal = FilterModal.Country },
        )
      }

      if (uiState.availableFilters.contains(DiscoverFilterType.Rating)) {
        item {
          DiscoverFilterChip.VoteAverage(
            modifier = Modifier
              .animateItem()
              .animateContentSize(),
            votes = uiState.filters.votes,
            voteAverage = uiState.filters.voteAverage,
            onClick = { filterModal = FilterModal.VoteAverage },
          )
        }
      }

      item {
        DiscoverFilterChip.MultiSelect(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filters = uiState.filters.keywords,
          onClick = { filterModal = FilterModal.Keywords },
          title = UIText.ResourceText(UiString.core_ui_keywords),
          name = uiState.filters.keywords.firstOrNull()?.name,
        )
      }

      item {
        DiscoverFilterChip.MultiSelect(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filters = uiState.filters.companies,
          onClick = { filterModal = FilterModal.Companies },
          title = UIText.PluralResource(UiPlurals.core_ui_information_companies, 2),
          name = uiState.filters.companies.firstOrNull()?.name,
        )
      }

      item {
        DiscoverFilterChip.MultiSelect(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          filters = uiState.filters.contentRating,
          onClick = { filterModal = FilterModal.ContentRating },
          title = UIText.ResourceText(UiString.core_ui_content_rating),
          name = uiState.filters.contentRating.firstOrNull()?.certification,
        )
      }
    }

    AnimatedVisibility(uiState.loading) {
      LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
      )
    }

    when (val form = uiState.form) {
      is DiscoverForm.Initial -> DiscoverInitialContent(mediaType)
      is DiscoverForm.Loading -> LoadingContent()
      is DiscoverForm.Error -> BlankSlate(
        modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
        uiState = form.blankSlate,
        onRetry = { viewModel.onAction(DiscoverAction.DiscoverMedia) },
      )
      is DiscoverForm.Data -> if (form.isEmpty) {
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
          items = form.media,
          section = uiState.section,
          onLoadMore = { viewModel.onAction(DiscoverAction.LoadMore) },
          onSwitchPreferences = onSwitchPreferences,
          onClick = { media -> media.toRoute()?.let { route -> onNavigate(route) } },
          onLongClick = { media ->
            onNavigate(Navigation.ActionMenuRoute.Media(media.encodeToString()))
          },
          canLoadMore = uiState.canFetchMore,
        )
      }
    }
  }
}
