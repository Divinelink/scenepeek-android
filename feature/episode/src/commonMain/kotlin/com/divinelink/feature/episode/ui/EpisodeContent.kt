package com.divinelink.feature.episode.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.resources.core_model_tab_guest_stars
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.collapsingheader.ui.DetailCollapsibleContent
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.credit.SmallPersonItem
import com.divinelink.core.ui.resources.core_ui_empty_plot
import com.divinelink.core.ui.tab.EpisodeTabs
import com.divinelink.feature.episode.EpisodeAction
import com.divinelink.feature.episode.EpisodeUiState
import com.divinelink.feature.episode.ui.provider.EpisodeUiStateParameterProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.model.resources.Res as modelRes

@OptIn(FlowPreview::class)
@Composable
fun SharedTransitionScope.EpisodeContent(
  visibilityScope: AnimatedVisibilityScope,
  uiState: EpisodeUiState,
  onBackdropLoaded: () -> Unit,
  toolbarProgress: (Float) -> Unit,
  onNavigate: (Navigation) -> Unit,
  action: (EpisodeAction) -> Unit,
) {
  val episode = uiState.episode ?: return

  val scope = rememberCoroutineScope()

  val pagerState = rememberPagerState(
    initialPage = uiState.selectedIndex,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .debounce { 100 }
      .distinctUntilChanged()
      .collectLatest { page ->
        action(EpisodeAction.OnSelectEpisode(page))
      }
  }

  DetailCollapsibleContent(
    visibilityScope = visibilityScope,
    backdropPath = episode.stillPath,
    posterPath = null,
    toolbarProgress = toolbarProgress,
    onBackdropLoaded = onBackdropLoaded,
    onNavigateToMediaPoster = { onNavigate(Navigation.MediaPosterRoute(it)) },
    headerContent = {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
      ) {
        EpisodeTitleDetails(
          uiState = uiState,
          episode = episode,
          onNavigate = onNavigate,
          action = action,
        )
      }
    },
    content = {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background),
      ) {
        EpisodeTabs(
          tabs = uiState.tabs,
          selectedIndex = uiState.selectedIndex,
          onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
        )

        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
        ) { page ->
          ScenePeekLazyColumn(
            modifier = Modifier
              .fillMaxSize(),
            contentPadding = PaddingValues(top = MaterialTheme.dimensions.keyline_16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
          ) {
            val episode = uiState.episodes[page]

            if (episode == null) {
              item {
                Box(modifier = Modifier.fillMaxSize()) {
                  LoadingContent(
                    modifier = Modifier.align(Alignment.TopCenter),
                  )
                }
              }
            } else {
              item {
                val overview = if (episode.overview.isNullOrEmpty()) {
                  stringResource(UiString.core_ui_empty_plot, episode.name)
                } else {
                  episode.overview ?: ""
                }

                Text(
                  modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
                  text = overview,
                  style = MaterialTheme.typography.bodyMedium,
                )
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
                )
              }

              if (episode.guestStars.isNotEmpty()) {
                item {
                  Text(
                    modifier = Modifier
                      .padding(bottom = MaterialTheme.dimensions.keyline_4)
                      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
                    text = stringResource(modelRes.string.core_model_tab_guest_stars),
                    style = MaterialTheme.typography.titleMedium,
                  )

                  LazyRow(
                    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_4),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                      MaterialTheme.dimensions.keyline_8,
                    ),
                  ) {
                    items(
                      items = episode.guestStars,
                      key = { it.id },
                    ) { person ->
                      SmallPersonItem(
                        person = person,
                        onClick = { onNavigate(it.toPersonRoute()) },
                      )
                    }
                  }
                }
              }
            }

            item {
              Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
            }
          }
        }
      }
    },
  )
}

@Composable
@Previews
fun EpisodeContentPreview(
  @PreviewParameter(EpisodeUiStateParameterProvider::class) state: EpisodeUiState,
) {
  PreviewLocalProvider {
    SharedTransitionScopeProvider { scope ->
      scope.EpisodeContent(
        visibilityScope = this,
        uiState = state,
        onBackdropLoaded = {},
        toolbarProgress = {},
        onNavigate = {},
        action = {},
      )
    }
  }
}
