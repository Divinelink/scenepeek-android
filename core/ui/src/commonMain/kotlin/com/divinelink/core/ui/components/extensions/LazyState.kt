package com.divinelink.core.ui.components.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * Handler to make any lazy column (or lazy row) infinite. Will notify the [onLoadMore]
 * callback once needed
 * @param lazyListState state of the list that needs to also be passed to the LazyColumn composable.
 * Get it by calling rememberLazyListState()
 * @param buffer the number of items before the end of the list to call the onLoadMore callback
 * @param onLoadMore will notify when we need to load more
 */
@Composable
fun LazyListState.EndlessScrollHandler(
  buffer: Int = 6,
  onLoadMore: () -> Unit,
) {
  val loadMore = remember {
    derivedStateOf {
      val layoutInfo = this.layoutInfo
      val visibleItemsInfo = layoutInfo.visibleItemsInfo
      val totalItems = layoutInfo.totalItemsCount
      val totalItemsFitInScreen = totalItems == visibleItemsInfo.size

      if (totalItemsFitInScreen) {
        false
      } else {
        val lastVisibleItemIndex = (visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
        lastVisibleItemIndex > (totalItems - buffer)
      }
    }
  }

  LaunchedEffect(loadMore) {
    snapshotFlow { loadMore.value to this@EndlessScrollHandler.layoutInfo.totalItemsCount }
      .distinctUntilChanged()
      .filter { it.first }
      .collect {
        onLoadMore()
      }
  }
}

@Composable
fun LazyGridState.EndlessScrollHandler(
  buffer: Int = 6,
  onLoadMore: () -> Unit,
) {
  val loadMore = remember {
    derivedStateOf {
      val layoutInfo = this.layoutInfo
      val visibleItemsInfo = layoutInfo.visibleItemsInfo
      val totalItems = layoutInfo.totalItemsCount
      val totalItemsFitInScreen = totalItems == visibleItemsInfo.size

      if (totalItemsFitInScreen) {
        false
      } else {
        val lastVisibleItemIndex = (visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
        lastVisibleItemIndex > (totalItems - buffer)
      }
    }
  }

  LaunchedEffect(loadMore) {
    snapshotFlow { loadMore.value to this@EndlessScrollHandler.layoutInfo.totalItemsCount }
      .distinctUntilChanged()
      .filter { it.first }
      .collect {
        onLoadMore()
      }
  }
}

@Composable
fun LazyGridState.canScrollToTop(): Boolean {
  val scrollToTop = remember {
    derivedStateOf {
      this.firstVisibleItemIndex > 3 && this.lastScrolledBackward
    }
  }

  return scrollToTop.value
}

@Composable
fun LazyListState.canScrollToTop(): Boolean {
  val scrollToTop = remember {
    derivedStateOf {
      this.firstVisibleItemIndex > 3 && this.lastScrolledBackward
    }
  }

  return scrollToTop.value
}

@Composable
fun LazyGridState.showExpandedFab(): Boolean {
  val scrollToTop = remember {
    derivedStateOf {
      this.firstVisibleItemIndex == 0 || this.lastScrolledBackward
    }
  }

  return scrollToTop.value
}

@Composable
fun LazyListState.showExpandedFab(): Boolean {
  val scrollToTop = remember {
    derivedStateOf {
      this.firstVisibleItemIndex == 0 || this.lastScrolledBackward
    }
  }

  return scrollToTop.value
}
