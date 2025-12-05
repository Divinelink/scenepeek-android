package com.divinelink.core.ui.collapsingheader

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import kotlin.jvm.JvmInline

private const val DEFAULT_SNAP_THRESHOLD = 0.25f
private const val HALF_PROGRESS = 0.5f

@Composable
fun rememberCollapsingHeaderState(
  collapsedHeight: Float,
  initialExpandedHeight: Float,
  snapThreshold: Float = DEFAULT_SNAP_THRESHOLD,
  initialStatus: CollapsingHeaderStatus = CollapsingHeaderStatus.Expanded,
  flingBehavior: FlingBehavior? = null,
): CollapsingHeaderState = rememberSaveable(
  saver = CollapsingHeaderState.saver(
    collapsedHeight = collapsedHeight,
    flingBehavior = flingBehavior,
  ),
  init = {
    CollapsingHeaderState(
      collapsedHeight = collapsedHeight,
      initialExpandedHeight = initialExpandedHeight,
      snapThreshold = snapThreshold,
      initialStatus = initialStatus,
      flingBehavior = flingBehavior,
    )
  },
)

@Stable
class CollapsingHeaderState(
  collapsedHeight: Float,
  initialExpandedHeight: Float,
  internal val snapThreshold: Float,
  initialStatus: CollapsingHeaderStatus = CollapsingHeaderStatus.Expanded,
  internal val flingBehavior: FlingBehavior? = null,
) {

  private var anchors by mutableLongStateOf(
    createAnchors(
      collapsedHeight = collapsedHeight,
      expandedHeight = initialExpandedHeight,
    ).packedValue,
  )

  /**
   * The height of the header when it is fully expanded.
   */
  var expandedHeight: Float
    get() = Anchors(anchors).expandedHeight
    internal set(value) {
      anchors = createAnchors(
        collapsedHeight = collapsedHeight,
        expandedHeight = value,
      ).packedValue
      updateAnchors()
    }

  /**
   * The height of the header when it is fully collapsed.
   */
  var collapsedHeight: Float
    get() = Anchors(anchors).collapsedHeight
    set(value) {
      anchors = createAnchors(
        collapsedHeight = value,
        expandedHeight = expandedHeight,
      ).packedValue
      updateAnchors()
    }

  val translation: Float
    get() = expandedHeight - anchoredDraggableState.requireOffset()

  val progress: Float
    get() {
      val range = (expandedHeight - collapsedHeight)
      return if (range == 0f) 0f else (translation / range).coerceIn(0f, 1f)
    }

  internal val anchoredDraggableState: AnchoredDraggableState<CollapsingHeaderStatus> =
    AnchoredDraggableState(
      initialValue = initialStatus,
      anchors = currentDraggableAnchors(),
    )

  private fun updateAnchors() = anchoredDraggableState.updateAnchors(
    currentDraggableAnchors(),
  )

  private fun currentDraggableAnchors(): DraggableAnchors<CollapsingHeaderStatus> =
    DraggableAnchors {
      CollapsingHeaderStatus.Collapsed at collapsedHeight
      CollapsingHeaderStatus.Expanded at expandedHeight
    }

  companion object {
    fun saver(
      collapsedHeight: Float,
      flingBehavior: FlingBehavior?,
    ) = listSaver<CollapsingHeaderState, Float>(
      save = { headerState ->
        listOf(
          headerState.expandedHeight,
          headerState.progress,
          headerState.snapThreshold,
        )
      },
      restore = { (expandedHeight, progress, snapThreshold) ->
        CollapsingHeaderState(
          collapsedHeight = collapsedHeight,
          initialExpandedHeight = expandedHeight,
          snapThreshold = snapThreshold,
          initialStatus = if (progress > HALF_PROGRESS) {
            CollapsingHeaderStatus.Collapsed
          } else {
            CollapsingHeaderStatus.Expanded
          },
          flingBehavior = flingBehavior,
        )
      },
    )
  }
}

@Immutable
@JvmInline
private value class Anchors(val packedValue: Long)

private fun createAnchors(
  collapsedHeight: Float,
  expandedHeight: Float,
) = Anchors(
  packFloats(
    val1 = collapsedHeight,
    val2 = expandedHeight,
  ),
)

private val Anchors.collapsedHeight: Float
  get() = unpackFloat1(packedValue)

private val Anchors.expandedHeight: Float
  get() = unpackFloat2(packedValue)
