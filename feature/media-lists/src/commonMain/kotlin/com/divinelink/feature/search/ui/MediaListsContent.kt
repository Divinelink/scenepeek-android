package com.divinelink.feature.search.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.feature.search.MediaListsAction
import com.divinelink.feature.search.MediaListsUiState
import com.divinelink.feature.search.ui.provider.MediaListsUiStateParameterProvider

@Composable
fun MediaListsContent(
  uiState: MediaListsUiState,
  action: (MediaListsAction) -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  when (uiState.form) {
    MediaListsForm.Error.Generic -> BlankSlate(
      modifier = Modifier
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Generic,
      onRetry = { action(MediaListsAction.Retry) },
    )
    MediaListsForm.Error.Offline -> BlankSlate(
      modifier = Modifier
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Offline,
      onRetry = { action(MediaListsAction.Retry) },
    )
    MediaListsForm.Initial -> LoadingContent()

    is MediaListsForm.Data -> ScrollableMediaContent(
      modifier = Modifier,
      items = uiState.form.media,
      section = ViewableSection.SEARCH,
      onLoadMore = { action(MediaListsAction.LoadMore) },
      onSwitchViewMode = onSwitchViewMode,
      onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
      onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
      canLoadMore = uiState.form.canLoadMore,
    )
  }
}

@Composable
@Previews
fun MediaListsContentPreview(
  @PreviewParameter(MediaListsUiStateParameterProvider::class) state: MediaListsUiState,
) {
  PreviewLocalProvider {
    MediaListsContent(
      uiState = state,
      action = {},
      onSwitchViewMode = {},
      onNavigate = {},
    )
  }
}
