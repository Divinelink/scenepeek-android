package com.divinelink.feature.search.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.media.MediaContent
import com.divinelink.core.ui.resources.searching
import com.divinelink.feature.search.resources.Res
import com.divinelink.feature.search.resources.feature_search__initial_description
import com.divinelink.feature.search.resources.feature_search__initial_title
import com.divinelink.feature.search.resources.search__empty_result_description
import com.divinelink.feature.search.resources.search__empty_result_title

@Composable
fun SearchContent(
  uiState: SearchUiState,
  onNavigate: (Navigation) -> Unit,
  onRetryClick: () -> Unit,
  onLoadNextPage: () -> Unit,
  scrollState: LazyGridState,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )

  HorizontalPager(
    modifier = Modifier.fillMaxSize(),
    state = pagerState,
  ) { page ->
    uiState.forms.values.elementAt(page).let {
      when (it) {
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
          uiState = it.blankSlate,
          onRetry = onRetryClick,
        )
        is SearchForm.Data -> if (it.isEmpty) {
          BlankSlate(
            modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
            uiState = BlankSlateState.Custom(
              icon = UiDrawable.searching,
              title = UIText.ResourceText(Res.string.search__empty_result_title),
              description = UIText.ResourceText(Res.string.search__empty_result_description),
            ),
          )
        } else {
          MediaContent(
            modifier = Modifier,
            section = MediaSection(
              data = it.media,
              shouldLoadMore = uiState.canFetchMore[uiState.selectedTab] == true,
            ),
            onMediaClick = { media ->
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
                else -> {
                  return@MediaContent
                }
              }
            },
            onLoadNextPage = onLoadNextPage,
            onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
            scrollState = scrollState,
          )
        }
      }
    }
  }
}
