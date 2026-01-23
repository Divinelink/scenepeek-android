package com.divinelink.feature.season.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.season.SeasonData
import com.divinelink.core.model.details.season.SeasonForm
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.collapsingheader.ui.DetailCollapsibleContent
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.season.SeasonAction
import com.divinelink.feature.season.SeasonUiState
import com.divinelink.feature.season.ui.components.SeasonTitleDetails
import com.divinelink.feature.season.ui.forms.about.AboutFormContent
import com.divinelink.feature.season.ui.forms.episodes.EpisodesFormContent
import com.divinelink.feature.season.ui.provider.SeasonUiStateParameterProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun SharedTransitionScope.SeasonContent(
  visibilityScope: AnimatedVisibilityScope,
  uiState: SeasonUiState,
  onBackdropLoaded: () -> Unit,
  toolbarProgress: (Float) -> Unit,
  onNavigate: (Navigation) -> Unit,
  action: (SeasonAction) -> Unit,
) {
  if (uiState.season == null) return
  val scope = rememberCoroutineScope()

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTab) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        action(SeasonAction.OnSelectTab(page))
      }
  }

  DetailCollapsibleContent(
    visibilityScope = visibilityScope,
    backdropPath = uiState.backdropPath,
    posterPath = uiState.season.posterPath,
    toolbarProgress = toolbarProgress,
    onBackdropLoaded = onBackdropLoaded,
    onNavigateToMediaPoster = { onNavigate(Navigation.MediaPosterRoute(it)) },
    headerContent = {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
      ) {
        SeasonTitleDetails(
          onNavigate = onNavigate,
          title = uiState.title,
          season = uiState.season,
        )

        uiState.season.status?.let {
          JellyseerrStatusPill(
            modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
            status = it,
          )
        }
      }
    },
    content = {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background),
      ) {
        ScenePeekTabs(
          tabs = uiState.tabs,
          selectedIndex = selectedPage,
          onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
        )

        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
        ) { page ->
          uiState.forms.values.elementAt(page).let { form ->
            when (form) {
              is SeasonForm.Content -> when (form.data) {
                is SeasonData.Episodes -> EpisodesFormContent(
                  data = form.data as SeasonData.Episodes,
                )
                is SeasonData.About -> AboutFormContent(
                  aboutData = form.data as SeasonData.About,
                )
                is SeasonData.Cast -> {
                }
              }

              SeasonForm.Error -> Column(
                modifier = Modifier
                  .fillMaxHeight()
                  .padding(vertical = MaterialTheme.dimensions.keyline_16)
                  .verticalScroll(rememberScrollState()),
              ) {
                BlankSlate(uiState = BlankSlateState.Contact)
              }

              SeasonForm.Loading -> LoadingContent(
                modifier = Modifier
                  .fillMaxSize()
                  .verticalScroll(rememberScrollState()),
              )
            }
          }
        }
      }
    },
  )
}

@Composable
@Previews
fun SeasonContentPreview(
  @PreviewParameter(SeasonUiStateParameterProvider::class) state: SeasonUiState,
) {
  SharedTransitionScopeProvider { scope ->
    scope.SeasonContent(
      visibilityScope = this,
      uiState = state,
      onBackdropLoaded = {},
      toolbarProgress = {},
      onNavigate = {},
      action = {},
    )
  }
}
