package com.divinelink.feature.home.nextup.fullscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.DisplayMessageDuration
import com.divinelink.core.ui.components.DisplayMessageSection
import com.divinelink.core.ui.resources.next_up_episodes
import com.divinelink.core.ui.resources.upcoming
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.home.nextup.NextUpAction
import com.divinelink.feature.home.nextup.NextUpEpisode
import com.divinelink.feature.home.nextup.NextUpViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.NextUpFullscreen(
  viewModel: NextUpViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  SnackbarMessageHandler(
    snackbarMessage = state.snackbarMessage,
    onDismissSnackbar = { viewModel.onAction(NextUpAction.DismissSnackbarMessage) },
  )

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        text = UIText.ResourceText(UiString.next_up_episodes),
        onNavigateUp = { onNavigate(Navigation.Back) },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        ScenePeekLazyColumn(
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          item {
            AnimatedVisibility(
              modifier = Modifier.fillMaxWidth(),
              visible = state.displayMessage != null,
            ) {
              DisplayMessageSection(
                message = state.displayMessage,
                duration = DisplayMessageDuration.INDEFINITE,
                onTimeout = { viewModel.onAction(NextUpAction.Retry) },
              )
            }
          }

          if (state.available.isEmpty() && state.upcoming.isEmpty()) {
            item {
            }
          }

          items(
            items = state.available,
            key = { episode -> episode.episode.showId },
          ) { episode ->
            NextUpEpisode(
              boxModifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
              nextUp = episode,
              section = NextUpSection.Available,
              action = viewModel::onAction,
              onNavigate = onNavigate,
            )
          }

          if (state.upcoming.isNotEmpty()) {
            item {
              Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
                text = stringResource(UiString.upcoming),
                style = MaterialTheme.typography.titleMedium,
              )
            }
            items(
              items = state.upcoming,
              key = { episode -> episode.episode.showId },
            ) { episode ->
              NextUpEpisode(
                boxModifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
                nextUp = episode,
                section = NextUpSection.Upcoming,
                action = viewModel::onAction,
                onNavigate = onNavigate,
              )
            }
          }

          item {
            Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
          }
        }
      }
    },
  )
}
