package com.divinelink.feature.watchlist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.NavigateUpButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.WatchlistScreen(
  onNavigateUp: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateToMediaDetails: (DetailsRoute) -> Unit,
  viewModel: WatchlistViewModel = koinViewModel(),
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  var selectedPage by rememberSaveable { mutableIntStateOf(0) }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      viewModel.onTabSelected(page)
      selectedPage = page
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .testTag(TestTags.Watchlist.WATCHLIST_SCREEN)
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = topAppBarColor,
        scrollBehavior = scrollBehavior,
        title = {
          Text(
            text = stringResource(uiR.string.core_ui_section_watchlist),
          )
        },
        navigationIcon = { NavigateUpButton(onClick = onNavigateUp) },
      )
    },
    floatingActionButton = {
      uiState.forms.values.elementAt(pagerState.currentPage).let {
        when (it) {
          is WatchlistForm.Error.Unauthenticated -> {
            ScaffoldFab(
              icon = Icons.Default.AccountCircle,
              text = stringResource(com.divinelink.core.ui.R.string.core_ui_login),
              expanded = true,
              onClick = onNavigateToTMDBLogin,
            )
          }
          else -> {
            // No FAB needed for other states
          }
        }
      }
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        Column {
          WatchlistTabs(
            tabs = uiState.tabs,
            selectedIndex = selectedPage,
            onClick = {
              scope.launch {
                pagerState.animateScrollToPage(it)
              }
            },
          )

          HorizontalPager(
            modifier = Modifier
              .fillMaxSize(),
            state = pagerState,
          ) { page ->
            uiState.forms.values.elementAt(page).let {
              when (it) {
                is WatchlistForm.Loading -> LoadingContent(
                  modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                )
                is WatchlistForm.Error -> WatchlistErrorContent(
                  error = it,
                  onRetry = viewModel::onRefresh,
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
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistTabs(
  tabs: List<WatchlistTab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  SecondaryTabRow(selectedTabIndex = selectedIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(
        modifier = Modifier.testTag(TestTags.Watchlist.TAB_BAR.format(tab.value)),
        text = {
          Text(
            text = stringResource(tab.titleRes),
            color = if (index == selectedIndex) {
              MaterialTheme.colorScheme.primary
            } else {
              MaterialTheme.colorScheme.onSurfaceVariant
            },
          )
        },
        selected = index == selectedIndex,
        onClick = { onClick(index) },
      )
    }
  }
}
