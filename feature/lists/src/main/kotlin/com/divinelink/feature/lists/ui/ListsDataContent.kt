package com.divinelink.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.feature.lists.ListsUiState
import com.divinelink.feature.lists.ListsUserInteraction
import com.divinelink.feature.lists.ui.provider.ListsUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun ListsDataContent(
  uiState: ListsUiState,
  userInteraction: (ListsUserInteraction) -> Unit,
) {
  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = { userInteraction(ListsUserInteraction.LoadMore) },
  )

  Box(Modifier.fillMaxSize()) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.Lists.SCROLLABLE_CONTENT),
      state = scrollState,
      contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_12),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
    }

    ScrollToTopButton(
      modifier = Modifier.align(Alignment.BottomCenter),
      visible = scrollState.canScrollToTop(),
      onClick = {
        scope.launch {
          scrollState.animateScrollToItem(0)
        }
      },
    )
  }
}

@Composable
@Previews
fun ListsContentPreview(
  @PreviewParameter(ListsUiStateParameterProvider::class) state: ListsUiState,
) {
  ListsDataContent(
    uiState = state,
    userInteraction = { },
  )
}
