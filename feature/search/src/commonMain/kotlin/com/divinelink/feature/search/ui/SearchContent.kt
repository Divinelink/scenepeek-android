package com.divinelink.feature.search.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.list.ScrollableMediaContentV2
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
) {
  uiState.forms[uiState.selectedTab]?.let { form ->
    when (form) {
      is SearchForm.Initial -> BlankSlate(
        modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
        uiState = BlankSlateState.Custom(
          icon = null,
          title = UIText.ResourceText(Res.string.feature_search__initial_title),
          description = UIText.ResourceText(Res.string.feature_search__initial_description),
        ),
      )
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
          )
          SearchTab.Movie -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchMovieTabState,
          )
          SearchTab.People -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchPeopleTabState,
          )
          SearchTab.TV -> SearchScrollableContent(
            form = form,
            onNavigate = onNavigate,
            onLoadNextPage = onLoadNextPage,
            canFetchMore = uiState.canFetchMore[uiState.selectedTab] == true,
            scrollState = searchTVTabState,
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
  scrollState: LazyGridState,
) {
  ScrollableMediaContentV2(
    state = scrollState,
    items = form.media,
    section = ViewableSection.DISCOVER, // TODO
    onLoadMore = onLoadNextPage,
    onSwitchViewMode = {
      // TODO
    },
    onClick = { media ->
      when (media) {
        is MediaItem.Media -> {
          val route = Navigation.DetailsRoute(
            id = media.id,
            mediaType = media.mediaType.value,
            isFavorite = media.isFavorite,
          )
          onNavigate(route)
        }
        is MediaItem.Person -> {
          val route = Navigation.PersonRoute(
            id = media.id.toLong(),
            knownForDepartment = media.knownForDepartment,
            name = media.name,
            profilePath = media.profilePath,
            gender = media.gender.value,
          )
          onNavigate(route)
        }
        else -> return@ScrollableMediaContentV2
      }
    },
    onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
    canLoadMore = canFetchMore,
    emptyContent = {
    },
  )
}
