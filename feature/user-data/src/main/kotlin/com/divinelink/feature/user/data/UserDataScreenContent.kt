package com.divinelink.feature.user.data

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.user.data.UserDataSection.Ratings
import com.divinelink.core.model.user.data.UserDataSection.Watchlist
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.media.MediaListContent
import kotlinx.coroutines.launch

@Composable
fun UserDataScreenContent(
  uiState: UserDataUiState,
  onRefresh: () -> Unit,
  onLoadMore: () -> Unit,
  onTabSelected: (Int) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      onTabSelected(page)
    }
  }

  Column {
    UserDataTabs(
      tabs = uiState.tabs,
      selectedIndex = uiState.selectedTabIndex,
      onClick = {
        scope.launch {
          pagerState.animateScrollToPage(it)
        }
      },
    )

    HorizontalPager(
      modifier = Modifier
        .fillMaxSize(),
      state = pagerState,
    ) { page ->
      uiState.forms.values.elementAt(page).let {
        when (it) {
          is UserDataForm.Loading -> LoadingContent()
          is UserDataForm.Error -> UserDataErrorContent(
            error = it,
            onRetry = onRefresh,
            section = uiState.section,
          )
          is UserDataForm.Data -> {
            if (it.isEmpty) {
              val stringResource = when (it.mediaType to uiState.section) {
                MediaType.MOVIE to Ratings -> R.string.feature_user_data_empty_movies_ratings
                MediaType.MOVIE to Watchlist -> R.string.feature_user_data_empty_movies_watchlist
                MediaType.TV to Ratings -> R.string.feature_user_data_empty_tv_shows_ratings
                MediaType.TV to Watchlist -> R.string.feature_user_data_empty_tv_shows_watchlist
                else -> R.string.feature_user_data_empty
              }
              BlankSlate(
                uiState = BlankSlateState.Custom(
                  title = UIText.ResourceText(stringResource),
                ),
              )
            } else {
              MediaListContent(
                list = it.media,
                onClick = { media ->
                  onNavigate(
                    Navigation.DetailsRoute(
                      mediaType = media.mediaType,
                      id = media.id,
                      isFavorite = media.isFavorite,
                    ),
                  )
                },
                onLoadMore = onLoadMore,
                onLongClick = { media ->
                  onNavigate(Navigation.ActionMenuRoute.Media(media.encodeToString()))
                },
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDataTabs(
  tabs: Map<MediaTab, Int?>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  SecondaryTabRow(selectedTabIndex = selectedIndex) {
    tabs.keys.forEachIndexed { index, tab ->
      Tab(
        modifier = Modifier.testTag(TestTags.Watchlist.TAB_BAR.format(tab.value)),
        text = {
          FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
          ) {
            Text(
              text = stringResource(tab.titleRes),
              color = if (index == selectedIndex) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant
              },
            )
            AnimatedVisibility(visible = tabs[tab] != null) {
              Text(
                modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_4),
                text = "(${tabs[tab] ?: 0})",
                color = if (index == selectedIndex) {
                  MaterialTheme.colorScheme.primary
                } else {
                  MaterialTheme.colorScheme.onSurfaceVariant
                },
              )
            }
          }
        },
        selected = index == selectedIndex,
        onClick = { onClick(index) },
      )
    }
  }
}
