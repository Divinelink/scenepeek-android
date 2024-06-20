package com.divinelink.watchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.watchlist.R
import com.divinelink.watchlist.navigation.WatchlistGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination<WatchlistGraph>(start = true)
internal fun WatchlistScreen(
  navigator: DestinationsNavigator,
  viewModel: WatchlistViewModel = hiltViewModel(),
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = scrollBehavior,
        title = {
          Text(stringResource(R.string.feature_watchlist_title))
        },
      )
    },
  ) { padding ->
    Surface(
      modifier = Modifier.padding(padding),
    ) {
      Column {
        WatchlistTabs(
          tabs = uiState.value.tabs,
          selectedIndex = selectedPage,
          onClick = {
            viewModel.onTabSelected(it)
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
              is WatchlistForm.Loading -> LoadingContent()
              is WatchlistForm.Error -> WatchlistErrorContent(
                error = it,
                onLogin = {
                  navigator.navigate(AccountSettingsScreenDestination)
                },
                onRetry = {
                },
              )
              is WatchlistForm.Data -> {
                if (it.isEmpty) {
                  WatchlistEmptyContent(it.emptyResultsUiText)
                } else {
                  WatchlistContent(
                    list = it.data,
                    onMediaClick = { media ->
                      navigator.navigate(
                        DetailsScreenDestination(
                          mediaType = media.mediaType.value,
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
          text = { Text(stringResource(tab.titleRes)) },
          selected = index == selectedIndex,
          onClick = { onClick(index) },
        )
      }
    }
  }
}

@Preview
@Composable
private fun WatchlistScreenPreview() {
  AppTheme {
//    Surface {
//      WatchlistScreen(
//        navigator = TODO(),
//        viewModel = hiltViewModel()
//      )
//    }
  }
}
