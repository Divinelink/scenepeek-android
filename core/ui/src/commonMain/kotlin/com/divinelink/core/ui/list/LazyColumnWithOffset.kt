package com.divinelink.core.ui.list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.PosterImage
import com.divinelink.core.ui.components.details.BackdropImage
import com.divinelink.core.ui.mediaImageDropShadow
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.LazyColumnWithOffset(
  backdropPath: String?,
  onBackdropLoaded: () -> Unit,
  posterPath: String?,
  visibilityScope: AnimatedVisibilityScope,
  onNavigateToPoster: (String) -> Unit = {},
  paddingOffset: Dp,
  stickyIndex: Int,
  state: LazyListState,
  onScrollUpdate: (Float) -> Unit,
  headerContent: @Composable () -> Unit,
  content: LazyListScope.() -> Unit,
) {
  val density = LocalDensity.current
  val previousFraction = remember { mutableFloatStateOf(0f) }
  val progress by remember {
    derivedStateOf {
      if (state.firstVisibleItemIndex > 0) {
        1f
      } else {
        val offset = state.firstVisibleItemScrollOffset.toFloat()
        val firstItemHeight = state.layoutInfo.visibleItemsInfo
          .firstOrNull()?.size?.toFloat() ?: 1f
        (offset / firstItemHeight).coerceIn(0f, 1f)
      }
    }
  }

  val offsetFraction by remember {
    derivedStateOf {
      when {
        state.firstVisibleItemIndex >= stickyIndex -> 1f
        state.firstVisibleItemIndex == stickyIndex - 1 -> {
          val itemInfo = state.layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == stickyIndex - 1 }
          if (itemInfo != null && itemInfo.size > 0) {
            val scrolled = -itemInfo.offset.toFloat()
            (scrolled / itemInfo.size).coerceIn(0f, 1f)
          } else {
            0f
          }
        }
        else -> 0f
      }
    }
  }

  LaunchedEffect(progress) {
    onScrollUpdate(progress)
  }

  LaunchedEffect(offsetFraction) {
    val delta = offsetFraction - previousFraction.floatValue
    if (delta != 0f) {
      with(density) {
        val pixels = delta * paddingOffset.roundToPx()
        state.dispatchRawDelta(pixels * 2)
      }
    }
    previousFraction.floatValue = offsetFraction
  }

  LazyColumn(
    state = state,
    contentPadding = PaddingValues(bottom = paddingOffset),
    modifier = Modifier
      .fillMaxSize()
      .testTag(TestTags.Details.COLLAPSIBLE_LAYOUT)
      .offset {
        IntOffset(0, (paddingOffset.roundToPx() * offsetFraction).roundToInt())
      }
      .layout { measurable, constraints ->
        val topBarHeightPx = paddingOffset.roundToPx()
        val placeable = measurable.measure(
          constraints.copy(maxHeight = constraints.maxHeight + topBarHeightPx),
        )
        layout(placeable.width, constraints.maxHeight) {
          placeable.place(0, -topBarHeightPx)
        }
      }
      .background(MaterialTheme.colorScheme.background),
  ) {
    item {
      BackdropImage(
        path = backdropPath,
        onBackdropLoaded = onBackdropLoaded,
      )
    }

    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = MaterialTheme.dimensions.keyline_16)
          .padding(horizontal = MaterialTheme.dimensions.keyline_16),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (posterPath != null) {
          PosterImage(
            modifier = Modifier
              .align(Alignment.CenterVertically)
              .sharedElement(
                sharedContentState = rememberSharedContentState(
                  SharedElementKeys.MediaPoster(posterPath),
                ),
                animatedVisibilityScope = visibilityScope,
              )
              .mediaImageDropShadow()
              .height(MaterialTheme.dimensions.posterSizeSmall)
              .aspectRatio(2f / 3f),
            path = posterPath,
            quality = ImageQuality.QUALITY_342,
            onClick = { onNavigateToPoster(it) },
          )
        }

        headerContent()
      }
    }

    content()
  }
}
