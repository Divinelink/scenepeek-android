package com.divinelink.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.core.ui.resources.searching
import com.divinelink.feature.search.resources.Res
import com.divinelink.feature.search.resources.feature_search__initial_description
import com.divinelink.feature.search.resources.feature_search__initial_title
import com.divinelink.feature.search.resources.search__empty_result_description
import com.divinelink.feature.search.resources.search__empty_result_title

@Suppress("NestedBlockDepth")
@Composable
fun SearchContent(
  uiState: SearchUiState,
  onNavigate: (Navigation) -> Unit,
  onRetryClick: () -> Unit,
  onLoadNextPage: () -> Unit,
  searchAllTabState: LazyGridState,
  searchMovieTabState: LazyGridState,
  searchPeopleTabState: LazyGridState,
  searchTVTabState: LazyGridState,
  historyState: LazyListState,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  uiState.forms[uiState.selectedTab]?.let { form ->
    when (form) {
      is SearchForm.Initial -> if (uiState.history.isEmpty()) {
        BlankSlate(
          modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
          uiState = BlankSlateState.Custom(
            icon = null,
            title = UIText.ResourceText(Res.string.feature_search__initial_title),
            description = UIText.ResourceText(Res.string.feature_search__initial_description),
          ),
        )
      } else {
        ScenePeekLazyColumn(
          state = historyState,
        ) {
          item {
            Text(
              modifier = Modifier.padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_6,
              ),
              text = "Recent searches",
              style = MaterialTheme.typography.titleSmall,
            )
          }
          items(uiState.history) { item ->
            Row(
              modifier = Modifier
                .clickable { item.toRoute()?.let { route -> onNavigate(route) } }
                .padding(MaterialTheme.dimensions.keyline_16)
                .fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
                imageVector = Icons.Default.History,
                contentDescription = null,
              )
              Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
              ) {
                Text(
                  text = item.name,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                )
              }
            }
          }

          item {
            Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
          }
        }
      }
      is SearchForm.Loading -> LoadingContent()
      is SearchForm.Error -> BlankSlate(
        modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
        uiState = form.blankSlate,
        onRetry = onRetryClick,
      )
      is SearchForm.Data -> if (form.isEmpty) {
        BlankSlate(
          modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
          uiState = BlankSlateState.Custom(
            icon = UiDrawable.searching,
            title = UIText.ResourceText(Res.string.search__empty_result_title),
            description = UIText.ResourceText(Res.string.search__empty_result_description),
          ),
        )
      } else {
        when (uiState.selectedTab) {
          SearchTab.All -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchAllTabState,
            onSwitchPreferences = onSwitchPreferences,
          )
          SearchTab.Movie -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchMovieTabState,
            onSwitchPreferences = onSwitchPreferences,
          )
          SearchTab.People -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchPeopleTabState,
            onSwitchPreferences = onSwitchPreferences,
          )
          SearchTab.TV -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchTVTabState,
            onSwitchPreferences = onSwitchPreferences,
          )
        }
      }
    }
  }
}

@Composable
private fun SearchScrollableContent(
  form: SearchForm.Data<MediaItem.Media>,
  onNavigate: (Navigation) -> Unit,
  onLoadNextPage: () -> Unit,
  canFetchMore: Boolean,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  scrollState: LazyGridState,
) {
  ScrollableMediaContent(
    modifier = Modifier,
    state = scrollState,
    items = form.media,
    section = ViewableSection.SEARCH,
    onLoadMore = onLoadNextPage,
    onSwitchPreferences = onSwitchPreferences,
    onClick = { media -> media.toRoute()?.let { onNavigate(it) } },
    onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
    canLoadMore = canFetchMore,
  )
}
