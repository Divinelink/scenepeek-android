package com.divinelink.feature.discover.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.media.MediaListContent
import com.divinelink.core.ui.tab.ScenePeekSecondaryTabs
import com.divinelink.feature.discover.DiscoverAction
import com.divinelink.feature.discover.DiscoverForm
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.R
import com.divinelink.feature.discover.genre.SelectGenreModalBottomSheet
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun DiscoverContent(
  uiState: DiscoverUiState,
  action: (DiscoverAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = uiState.selectedTabIndex,
    pageCount = { uiState.tabs.size },
  )
  var showGenreModal by remember { mutableStateOf(false) }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      action.invoke(DiscoverAction.OnSelectTab(page))
    }
  }

  if (showGenreModal) {
    SelectGenreModalBottomSheet(
      mediaType = uiState.selectedTab.mediaType,
      onDismissRequest = { showGenreModal = false },
    )
  }

  Column {
    ScenePeekSecondaryTabs(
      tabs = uiState.tabs,
      selectedIndex = uiState.selectedTabIndex,
      onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
    )

    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
      contentPadding = PaddingValues(
        vertical = MaterialTheme.dimensions.keyline_4,
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
    ) {
      item {
        FilterChip(
          selected = uiState.selectedGenreFilters.isNotEmpty(),
          label = {
            val text = when {
              uiState.selectedGenreFilters.isEmpty() -> stringResource(UiString.core_ui_genres)
              uiState.selectedGenreFilters.size == 1 -> uiState.selectedGenreFilters.first().name
              else -> buildString {
                append(uiState.selectedGenreFilters.first().name)
                append("+")
                append(uiState.selectedGenreFilters.size - 1)
              }
            }

            Text(
              text = text,
              style = MaterialTheme.typography.titleSmall,
            )
          },
          trailingIcon = {
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = null,
            )
          },
          onClick = { showGenreModal = true },
        )
      }
    }

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
    ) { page ->
      uiState.forms.values.elementAt(page).let {
        when (it) {
          is DiscoverForm.Initial -> DiscoverInitialContent(tab = uiState.selectedTab)
          is DiscoverForm.Loading -> LoadingContent()
          is DiscoverForm.Error -> BlankSlate(
            uiState = it.blankSlate,
            onRetry = {},
          )
          is DiscoverForm.Data -> {
            if (it.isEmpty) {
              BlankSlate(
                uiState = BlankSlateState.Custom(
                  title = UIText.ResourceText(R.string.feature_discover_empty_result),
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
                onLoadMore = { Unit },
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

@Composable
@Previews
fun DiscoverContentPreview(
  @PreviewParameter(DiscoverUiStateParameterProvider::class) state: DiscoverUiState,
) {
  PreviewLocalProvider {
    DiscoverContent(
      uiState = state,
      action = {},
      onNavigate = {},
    )
  }
}
