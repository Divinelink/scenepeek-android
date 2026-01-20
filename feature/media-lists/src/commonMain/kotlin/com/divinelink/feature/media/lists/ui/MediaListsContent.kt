package com.divinelink.feature.media.lists.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.core.ui.resources.no_results
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.media.lists.MediaListsAction
import com.divinelink.feature.media.lists.MediaListsUiState
import com.divinelink.feature.media.lists.resources.Res
import com.divinelink.feature.media.lists.resources.empty_favorite_list_description
import com.divinelink.feature.media.lists.resources.empty_favorite_list_title
import com.divinelink.feature.media.lists.resources.empty_media_list_description
import com.divinelink.feature.media.lists.resources.empty_media_list_title
import com.divinelink.feature.media.lists.ui.provider.MediaListsUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun MediaListsContent(
  uiState: MediaListsUiState,
  action: (MediaListsAction) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTab.order,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      action.invoke(MediaListsAction.OnSelectTab(page))
    }
  }

  Column {
    if (uiState.showTabs) {
      ScenePeekSecondaryTabs(
        tabs = uiState.tabs,
        selectedIndex = uiState.selectedTab.order,
        onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
      )
    }

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
    ) { page ->
      uiState.forms.values.elementAt(page).let { form ->
        when (form) {
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

          is MediaListsForm.Data -> if (form.media.isEmpty()) {
            val state = if (uiState.section is MediaListSection.Favorites) {
              BlankSlateState.Custom(
                icon = UiDrawable.no_results,
                title = UIText.ResourceText(Res.string.empty_favorite_list_title),
                description = UIText.ResourceText(Res.string.empty_favorite_list_description),
              )
            } else {
              BlankSlateState.Custom(
                icon = UiDrawable.no_results,
                title = UIText.ResourceText(Res.string.empty_media_list_title),
                description = UIText.ResourceText(Res.string.empty_media_list_description),
              )
            }

            BlankSlate(
              modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
              uiState = state,
            )
          } else {
            ScrollableMediaContent(
              modifier = Modifier,
              items = form.media,
              section = ViewableSection.SEARCH,
              onLoadMore = { action(MediaListsAction.LoadMore) },
              onSwitchPreferences = onSwitchPreferences,
              onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
              onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
              canLoadMore = form.canLoadMore,
            )
          }
        }
      }
    }
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
      onSwitchPreferences = {},
      onNavigate = {},
    )
  }
}
