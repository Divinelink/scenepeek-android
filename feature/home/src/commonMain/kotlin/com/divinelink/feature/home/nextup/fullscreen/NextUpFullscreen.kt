package com.divinelink.feature.home.nextup.fullscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.DisplayMessageDuration
import com.divinelink.core.ui.components.DisplayMessageSection
import com.divinelink.core.ui.resources.core_ui_discover
import com.divinelink.core.ui.resources.show_progress_empty_state_subtitle
import com.divinelink.core.ui.resources.show_progress_empty_state_title
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.home.nextup.NextUpAction
import com.divinelink.feature.home.nextup.NextUpEpisode
import com.divinelink.feature.home.nextup.NextUpViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NextUpFullscreen(
  section: NextUpSection,
  viewModel: NextUpViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  SnackbarMessageHandler(
    snackbarMessage = state.snackbarMessage,
    onDismissSnackbar = { viewModel.onAction(NextUpAction.DismissSnackbarMessage) },
  )

  when (section) {
    NextUpSection.Available -> if (state.available.isEmpty()) {
      BlankSlate(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        uiState = BlankSlateState.Custom(
          title = UIText.ResourceText(UiString.show_progress_empty_state_title),
          description = UIText.ResourceText(UiString.show_progress_empty_state_subtitle),
          retryText = UIText.ResourceText(UiString.core_ui_discover),
        ),
        onRetry = {
          onNavigate(
            Navigation.Discover.Custom(
              mediaType = MediaType.TV,
              entryPoint = DiscoverType.TopRated,
            ),
          )
        },
      )
    }
    NextUpSection.Upcoming -> Unit
    NextUpSection.Paused -> Unit
  }

  ScenePeekLazyColumn(
    modifier = Modifier.fillMaxSize(),
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

    items(
      items = when (section) {
        NextUpSection.Available -> state.available
        NextUpSection.Upcoming -> state.upcoming
        NextUpSection.Paused -> state.paused
      },
      key = { episode -> episode.episode.showId },
    ) { episode ->
      NextUpEpisode(
        boxModifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
        nextUp = episode,
        section = section,
        action = viewModel::onAction,
        onNavigate = onNavigate,
      )
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
