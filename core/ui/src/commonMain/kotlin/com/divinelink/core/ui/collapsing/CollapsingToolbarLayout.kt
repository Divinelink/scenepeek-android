package com.divinelink.core.ui.collapsing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.divinelink.core.ui.conditional

/**
 * Kudos: https://github.com/gaiuszzang/GroovinCollapsingToolBar
 */

@Composable
fun CollapsingToolBarLayout(
  modifier: Modifier = Modifier,
  state: CollapsingToolBarState,
  updateToolBarHeightManually: Boolean = false,
  toolbar: @Composable CollapsingToolBarLayoutToolBarScope.() -> Unit,
  content: @Composable CollapsingToolBarLayoutContentScope.() -> Unit,
) {
  val coroutineScope = rememberCoroutineScope()
  val nestedScrollConnection = remember(state) {
    CollapsingLayoutNestedScrollConnection(state, coroutineScope)
  }
  val configuration = LocalCollapsingToolBarLayoutConfiguration.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(nestedScrollConnection)
      .then(modifier),
  ) {
    // ToolBar
    val toolBarCollapsedInfo = ToolBarCollapsedInfo(state.progress, state.toolBarHeight)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.Top, unbounded = true)
        .then(if (!updateToolBarHeightManually) Modifier.height(state.toolBarHeight) else Modifier),
    ) {
      CollapsingToolBarLayoutToolBarScope(state, toolBarCollapsedInfo).toolbar()
    }
    // Content
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
    if (
      state.collapsingOption.collapsingWhenTop &&
      LocalOverscrollFactory.current != null
    ) {
      // BugFix
      // Since Android 12(S), Overscroll effect consumes scroll event first.
      // So, Disable Overscroll effect in >= Android 12 version case and
      // Use the Internal Stretch Effect with according to configurations.
      CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .conditional(
              configuration.collapsingWhenTopConfiguration.useInternalStretchEffectOnTop,
            ) {
              topStretchEffect()
            }
            .conditional(
              configuration.collapsingWhenTopConfiguration.useInternalStretchEffectOnBottom,
            ) {
              bottomStretchEffect()
            },
        ) {
          CollapsingToolBarLayoutContentScope(state).content()
        }
      }
    } else {
      Box(
        modifier = Modifier.fillMaxSize(),
      ) {
        CollapsingToolBarLayoutContentScope(state).content()
      }
    }
  }
}

private fun Modifier.topStretchEffect(stretchMultiplier: Float = STRETCH_MULTIPLIER) = composed {
  val topOverPullState = rememberTopOverPullState()
  val overStretchScale = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    snapshotFlow {
      topOverPullState.overPullDistance.coerceAtLeast(0f)
    }.collect { overPullDistance ->
      val nextScale = overPullDistance * stretchMultiplier
      if (nextScale == 0f && overStretchScale.value != 0f) {
        overStretchScale.animateTo(targetValue = 0f, animationSpec = tween(250))
      } else {
        overStretchScale.snapTo(nextScale)
      }
    }
  }
  return@composed this
    .topOverPull(topOverPullState)
    .clipToBounds()
    .graphicsLayer(
      scaleY = (overStretchScale.value + 1f),
      transformOrigin = TransformOrigin(0f, 0f),
    )
}

private fun Modifier.bottomStretchEffect(stretchMultiplier: Float = STRETCH_MULTIPLIER) = composed {
  val bottomOverPullState = rememberBottomOverPullState()
  val overStretchScale = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    snapshotFlow {
      bottomOverPullState.overPullDistance.coerceAtLeast(0f)
    }.collect { overPullDistance ->
      val nextScale = overPullDistance * stretchMultiplier
      if (nextScale == 0f && overStretchScale.value != 0f) {
        overStretchScale.animateTo(targetValue = 0f, animationSpec = tween(250))
      } else {
        overStretchScale.snapTo(nextScale)
      }
    }
  }
  return@composed this
    .bottomOverPull(bottomOverPullState)
    .clipToBounds()
    .graphicsLayer(
      scaleY = (overStretchScale.value + 1f),
      transformOrigin = TransformOrigin(0f, 1f),
    )
}

internal const val STRETCH_MULTIPLIER = 0.000015f
