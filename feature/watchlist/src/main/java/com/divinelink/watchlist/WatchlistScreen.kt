package com.divinelink.watchlist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
  uiState: WatchlistUiState
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(R.string.watchlist_title))
        }
      )
    }) { padding ->
    Surface(
      modifier = Modifier.padding(padding),
    ) {
      WatchlistTabs(uiState.tabs)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistTabs(
  tabs: List<WatchlistTab>
) {
  Row {
    SecondaryTabRow(selectedTabIndex = 0) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          text = { Text(stringResource(tab.titleRes)) },
          selected = index == 0,
          onClick = { /* TODO: Handle tab selection */ }
        )
      }
    }
  }
}

@Preview
@Composable
private fun WatchlistScreenPreview() {
  MaterialTheme {
    Surface {
      WatchlistScreen(
        uiState = WatchlistUiState(
          tabs = WatchlistTab.entries,
          movies = Tab.Loading,
          tvShows = Tab.Loading
        )
      )
    }
  }
}
