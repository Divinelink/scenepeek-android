package com.divinelink.core.ui.collapsingheader.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.collapsingheader.CollapsingHeaderLayout
import com.divinelink.core.ui.collapsingheader.rememberCollapsingHeaderState

@Composable
fun SharedTransitionScope.DetailCollapsibleContent(
  visibilityScope: AnimatedVisibilityScope,
  backdropPath: String?,
  posterPath: String?,
  toolbarProgress: (Float) -> Unit,
  onBackdropLoaded: () -> Unit,
  onNavigateToMediaPoster: (String) -> Unit,
  headerContent: @Composable () -> Unit,
  content: @Composable () -> Unit,
) {
  val density = LocalDensity.current

  val collapsingHeaderState = rememberCollapsingHeaderState(
    collapsedHeight = with(density) { MaterialTheme.dimensions.keyline_0.toPx() },
    initialExpandedHeight = with(density) { 400.dp.toPx() },
  )

  LaunchedEffect(collapsingHeaderState.progress) {
    toolbarProgress(collapsingHeaderState.progress)
  }

  CollapsingHeaderLayout(
    modifier = Modifier
      .testTag(TestTags.Details.COLLAPSIBLE_LAYOUT)
      .fillMaxSize(),
    state = collapsingHeaderState,
    headerContent = {
      CollapsibleHeaderContent(
        collapsingHeaderState = collapsingHeaderState,
        backdropPath = backdropPath,
        posterPath = posterPath,
        onBackdropLoaded = onBackdropLoaded,
        visibilityScope = visibilityScope,
        onNavigateToMediaPoster = onNavigateToMediaPoster,
        content = { headerContent() },
      )
    },
    body = { content() },
  )
}
