package com.divinelink.feature.home.progress

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.UIText
import com.divinelink.core.model.tab.ProgressTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.model.tab.TabsScreenEntryPoint
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.resources.next_up_episodes
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.core.ui.tabbed.TabsScreenViewModel
import com.divinelink.feature.home.history.WatchHistoryContent
import com.divinelink.feature.home.nextup.fullscreen.NextUpFullscreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.ProgressScreen(
  tab: Tab,
  viewModel: TabsScreenViewModel = koinViewModel {
    parametersOf(
      TabsScreenEntryPoint.NextUp,
      tab,
      ProgressTab.entries,
    )
  },
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val scope = rememberCoroutineScope()

  val pagerState = rememberPagerState(
    initialPage = state.selectedTabIndex,
    pageCount = { state.tabs.size },
  )

  LaunchedEffect(state.selectedTabIndex) {
    val target = state.selectedTabIndex
    if (target in state.tabs.indices && target != pagerState.currentPage) {
      pagerState.animateScrollToPage(target)
    }
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .collectLatest {
        viewModel.onSelectTab(it)
      }
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        text = UIText.ResourceText(UiString.next_up_episodes),
        onNavigateUp = { onNavigate(Navigation.Back) },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        ScenePeekTabs(
          selectedIndex = state.selectedTabIndex,
          tabs = state.tabs,
          onClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
        )

        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
          key = { page -> state.tabs.elementAt(page).toString() },
        ) { page ->
          val tab = state.tabs.elementAt(page)

          when (tab) {
            ProgressTab.Progress -> NextUpFullscreen(
              section = NextUpSection.Available,
              onNavigate = onNavigate,
            )
            ProgressTab.Upcoming -> NextUpFullscreen(
              section = NextUpSection.Upcoming,
              onNavigate = onNavigate,
            )
            ProgressTab.History -> WatchHistoryContent(
              onNavigate = onNavigate,
            )
            ProgressTab.OnHold -> NextUpFullscreen(
              section = NextUpSection.Paused,
              onNavigate = onNavigate,
            )
            else -> Unit
          }
        }
      }
    },
  )
}
