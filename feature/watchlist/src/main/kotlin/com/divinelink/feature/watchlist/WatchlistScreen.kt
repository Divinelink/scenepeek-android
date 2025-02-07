package com.divinelink.feature.watchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.scaffold.AppScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
  onNavigateToAccountSettings: () -> Unit,
  onNavigateToMediaDetails: (DetailsRoute) -> Unit,
  viewModel: WatchlistViewModel = koinViewModel(),
) {
  var selectedPage by rememberSaveable { mutableIntStateOf(0) }
  val uiState = viewModel.uiState.collectAsState()

  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.value.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      viewModel.onTabSelected(page)
      selectedPage = page
    }
  }

  AppScaffold(
    modifier = Modifier.testTag(TestTags.Watchlist.WATCHLIST_SCREEN),
    topBar = { scrollBehavior, topAppBarColors ->
      TopAppBar(
        colors = topAppBarColors,
        scrollBehavior = scrollBehavior,
        title = {
          Text(stringResource(R.string.feature_watchlist_title))
        },
      )
    },
  ) {
    Column {
      WatchlistTabs(
        tabs = uiState.value.tabs,
        selectedIndex = selectedPage,
        onClick = {
          scope.launch {
            pagerState.animateScrollToPage(it)
          }
        },
      )

      HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
      ) { page ->
        uiState.value.forms.values.elementAt(page).let {
          when (it) {
            is WatchlistForm.Loading -> LoadingContent(
              modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
            )
            is WatchlistForm.Error -> WatchlistErrorContent(
              error = it,
              onLogin = onNavigateToAccountSettings,
              onRetry = {
              },
            )
            is WatchlistForm.Data -> {
              if (it.isEmpty) {
                BlankSlate(uiState = BlankSlateState.Custom(title = it.emptyResultsUiText))
              } else {
                WatchlistContent(
                  list = it.data,
                  onMediaClick = { media ->
                    onNavigateToMediaDetails(
                      DetailsRoute(
                        mediaType = media.mediaType,
                        id = media.id,
                        isFavorite = media.isFavorite,
                      ),
                    )
                  },
                  totalResults = it.totalResultsUiText,
                  onLoadMore = viewModel::onLoadMore,
                )
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistTabs(
  tabs: List<WatchlistTab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  Row {
    SecondaryTabRow(selectedTabIndex = selectedIndex) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          modifier = Modifier.testTag(TestTags.Watchlist.TAB_BAR.format(tab.value)),
          text = { Text(stringResource(tab.titleRes)) },
          selected = index == selectedIndex,
          onClick = { onClick(index) },
        )
      }
    }
  }
}
