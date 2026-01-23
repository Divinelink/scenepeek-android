package com.divinelink.feature.season.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.collapsingheader.ui.DetailCollapsibleContent
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.feature.season.SeasonAction
import com.divinelink.feature.season.SeasonUiState
import com.divinelink.feature.season.ui.components.SeasonTitleDetails
import com.divinelink.feature.season.ui.provider.SeasonUiStateParameterProvider

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
