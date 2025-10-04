package com.divinelink.feature.discover.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.discover.DiscoverAction
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun DiscoverContent(
  uiState: DiscoverUiState,
  action: (DiscoverAction) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      action.invoke(DiscoverAction.OnSelectTab(page))
    }
  }

  Column {
    ScenePeekSecondaryTabs(
      tabs = uiState.tabs,
      selectedIndex = uiState.selectedTabIndex,
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
      action = { },
    )
  }
}
