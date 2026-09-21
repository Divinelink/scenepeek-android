package com.divinelink.feature.home.nextup.modal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.DateFormatStyle
import com.divinelink.core.commons.formatLocalized
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.details.BackdropImage
import com.divinelink.core.ui.modal.ScenePeekBottomSheet
import com.divinelink.core.ui.tab.EpisodeTabs
import com.divinelink.feature.episode.EpisodeAction
import com.divinelink.feature.episode.EpisodeViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun EpisodesOverviewModal(
  route: Navigation.Episode.ModalRoute,
  onDismissRequest: () -> Unit,
  viewModel: EpisodeViewModel = koinViewModel { parametersOf(route) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val scope = rememberCoroutineScope()

  val pagerState = rememberPagerState(
    initialPage = state.selectedIndex,
    pageCount = { state.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .debounce { 100 }
      .distinctUntilChanged()
      .collectLatest { page ->
        viewModel.onAction(EpisodeAction.OnSelectEpisode(page))
      }
  }

  ScenePeekBottomSheet(
    modifier = Modifier,
    isVisible = true,
    onDismissRequest = onDismissRequest,
    dragHandle = null,
  ) {
    BackdropImage(
      path = state.episode?.stillPath,
      onBackdropLoaded = {},
    )

    EpisodeTabs(
      tabs = state.tabs,
      selectedIndex = state.selectedIndex,
      onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
    )

    HorizontalPager(
      modifier = Modifier
        .animateContentSize()
        .background(MaterialTheme.colorScheme.background),
      state = pagerState,
    ) { page ->
      ScenePeekLazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(top = MaterialTheme.dimensions.keyline_16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        val episode = state.episodes[page]

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
            Text(
              modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
              text = episode.name,
              style = MaterialTheme.typography.titleMedium,
            )
          }

          item {
            Text(
              modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
              text = episode.airDate?.formatLocalized(DateFormatStyle.FULL) ?: "",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }


          if (!episode.overview.isNullOrEmpty()) {
            item {
              Text(
                modifier = Modifier
                  .padding(top = MaterialTheme.dimensions.keyline_12)
                  .padding(horizontal = MaterialTheme.dimensions.keyline_16),
                text = episode.overview ?: "",
                style = MaterialTheme.typography.bodyMedium,
              )
            }
          }

          item {
            Spacer(
              modifier = Modifier
                .padding(MaterialTheme.dimensions.keyline_16)
                .height(LocalBottomNavigationPadding.current),
            )
          }
        }
      }
    }
  }
}
