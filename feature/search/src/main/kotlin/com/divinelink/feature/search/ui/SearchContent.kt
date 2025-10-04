package com.divinelink.feature.search.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.media.MediaContent
import com.divinelink.feature.search.R

@Composable
fun SearchContent(
  uiState: SearchUiState,
  onNavigate: (Navigation) -> Unit,
  onRetryClick: () -> Unit,
  onLoadNextPage: () -> Unit,
  scrollState: LazyGridState,
) {
  when {
    uiState.error is BlankSlateState.Offline -> BlankSlate(
      modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Offline,
      onRetry = onRetryClick,
    )

    uiState.searchResults?.data?.isEmpty() == true -> BlankSlate(
      modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Custom(
        icon = com.divinelink.core.ui.R.drawable.core_ui_search,
        title = UIText.ResourceText(R.string.search__empty_result_title),
        description = UIText.ResourceText(R.string.search__empty_result_description),
      ),
    )

    uiState.searchResults?.data?.isNotEmpty() == true -> MediaContent(
      modifier = Modifier,
      section = uiState.searchResults,
      onMediaClick = { media ->
        when (media) {
          is MediaItem.Media -> {
            val route = Navigation.DetailsRoute(
              id = media.id,
              mediaType = media.mediaType,
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
              gender = media.gender,
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

    else -> BlankSlate(
      modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
      uiState = BlankSlateState.Custom(
        icon = null,
        title = UIText.ResourceText(R.string.feature_search__initial_title),
        description = UIText.ResourceText(R.string.feature_search__initial_description),
      ),
    )
  }
}
