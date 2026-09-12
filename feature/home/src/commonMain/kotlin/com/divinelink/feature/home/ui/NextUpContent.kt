package com.divinelink.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.components.DisplayMessageDuration
import com.divinelink.core.ui.components.DisplayMessageSection
import com.divinelink.feature.home.nextup.EpisodesList
import com.divinelink.feature.home.nextup.NextUpAction
import com.divinelink.feature.home.nextup.NextUpEmptyContent
import com.divinelink.feature.home.nextup.NextUpViewModel
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NextUpContent(
  modifier: Modifier = Modifier,
  viewModel: NextUpViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

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

  if (state.loading) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(MaterialTheme.dimensions.posterSizeSmall)
        .padding(horizontal = MaterialTheme.dimensions.keyline_8)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  } else {
    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      if (state.available.isEmpty() && state.upcoming.isEmpty()) {
        NextUpEmptyContent()
      }

      EpisodesList(
        modifier = modifier,
        episodes = state.available,
        action = viewModel::onAction,
        section = NextUpSection.Available,
        onNavigate = onNavigate,
      )

      if (state.upcoming.isNotEmpty()) {
        EpisodesList(
          modifier = modifier,
          episodes = state.upcoming,
          section = NextUpSection.Upcoming,
          action = viewModel::onAction,
          onNavigate = onNavigate,
        )
      }
    }
  }
}
