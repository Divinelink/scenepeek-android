package com.divinelink.feature.collections.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.collapsingheader.ui.DetailCollapsibleContent
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.core.ui.text.SimpleExpandingText
import com.divinelink.feature.collections.CollectionsAction
import com.divinelink.feature.collections.CollectionsUiState
import com.divinelink.feature.collections.ui.provider.CollectionsUiStateParameterProvider

@Composable
fun SharedTransitionScope.CollectionsContent(
  visibilityScope: AnimatedVisibilityScope,
  uiState: CollectionsUiState,
  topPadding: Dp,
  onBackdropLoaded: () -> Unit,
  toolbarProgress: (Float) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  onAction: (CollectionsAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  DetailCollapsibleContent(
    visibilityScope = visibilityScope,
    backdropPath = uiState.backdropPath,
    posterPath = uiState.posterPath,
    toolbarProgress = toolbarProgress,
    onBackdropLoaded = onBackdropLoaded,
    heightDifference = topPadding,
    onNavigateToMediaPoster = { onNavigate(Navigation.MediaPosterRoute(it)) },
    headerContent = {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        Text(
          text = uiState.collectionName,
          style = MaterialTheme.typography.titleLarge,
        )

        SimpleExpandingText(
          modifier = Modifier.fillMaxWidth(),
          style = MaterialTheme.typography.bodySmall,
          text = AnnotatedString(uiState.overview ?: ""),
        )
      }
    },
    content = {
      when {
        uiState.loading -> Box(modifier = Modifier.fillMaxSize()) {
          LoadingContent(
            modifier = Modifier
              .padding(top = MaterialTheme.dimensions.keyline_16)
              .align(Alignment.TopCenter)
              .verticalScroll(rememberScrollState()),
          )
        }

        uiState.error != null -> Column(
          modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = MaterialTheme.dimensions.keyline_16)
            .verticalScroll(rememberScrollState()),
        ) {
          BlankSlate(
            uiState = BlankSlateState.Contact,
            onRetry = { onAction(CollectionsAction.Refresh) },
          )
        }
        uiState.movies.isNotEmpty() -> ScrollableMediaContent(
          modifier = Modifier,
          items = uiState.movies,
          onLoadMore = { /* Do nothing */ },
          onSwitchPreferences = onSwitchPreferences,
          onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
          section = ViewableSection.SEARCH,
          onLongClick = { media ->
            onNavigate(Navigation.ActionMenuRoute.Media(media.encodeToString()))
          },
          canLoadMore = false,
        )
      }
    },
  )
}

@Composable
@Previews
fun CollectionsContentPreview(
  @PreviewParameter(CollectionsUiStateParameterProvider::class) state: CollectionsUiState,
) {
  PreviewLocalProvider {
    SharedTransitionScopeProvider { scope ->
      scope.CollectionsContent(
        visibilityScope = this,
        uiState = state,
        topPadding = MaterialTheme.dimensions.keyline_0,
        onBackdropLoaded = {},
        toolbarProgress = {},
        onSwitchPreferences = {},
        onAction = {},
        onNavigate = {},
      )
    }
  }
}
