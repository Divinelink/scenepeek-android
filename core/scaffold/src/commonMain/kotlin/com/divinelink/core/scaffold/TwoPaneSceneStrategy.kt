package com.divinelink.core.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND

/**
 * A custom [Scene] that displays two [NavEntry]s side-by-side in a 50/50 split.
 *
 * Both the first and second entries must declare support for two-pane display by including
 * [TwoPaneScene.twoPane] in their metadata.
 */
data class TwoPaneScene<T : Any>(
  override val key: Any,
  override val previousEntries: List<NavEntry<T>>,
  val firstEntry: NavEntry<T>,
  val secondEntry: NavEntry<T>,
) : Scene<T> {
  override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry)

  override val content: @Composable () -> Unit = {
    Row(modifier = Modifier.fillMaxSize()) {
      Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
        firstEntry.Content()
      }
      Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
        secondEntry.Content()
      }
    }
  }

  companion object {
    /**
     * Add to [NavEntry] metadata to declare support for two-pane display.
     * Both the master and detail entries must include this metadata for
     * [TwoPaneSceneStrategy] to activate.
     */
    fun twoPane(): Map<String, Any> = mapOf(TWO_PANE_KEY to true)

    internal const val TWO_PANE_KEY = "twoPaneLayout"
  }
}

/**
 * Returns a remembered [TwoPaneSceneStrategy] that reads the current window size class.
 * The strategy is recomputed whenever the window size class changes.
 */
@Composable
fun <T : Any> rememberTwoPaneSceneStrategy(): TwoPaneSceneStrategy<T> {
  val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
  return remember(windowSizeClass) {
    TwoPaneSceneStrategy(windowSizeClass)
  }
}

/**
 * A [SceneStrategy] that activates a [TwoPaneScene] on expanded screens when both
 * of the top two back stack entries declare support for two-pane display via [TwoPaneScene.twoPane].
 *
 * On smaller screens (width < [WIDTH_DP_EXPANDED_LOWER_BOUND]) returns `null` so the next
 * strategy in the chain takes over.
 */
class TwoPaneSceneStrategy<T : Any>(val windowSizeClass: WindowSizeClass) : SceneStrategy<T> {
  override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
    if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)) return null

    val lastTwoEntries = entries.takeLast(2)

    return if (
      lastTwoEntries.size == 2 &&
      lastTwoEntries.all { it.metadata[TwoPaneScene.TWO_PANE_KEY] == true }
    ) {
      val firstEntry = lastTwoEntries.first()
      val secondEntry = lastTwoEntries.last()
      TwoPaneScene(
        key = Pair(firstEntry.contentKey, secondEntry.contentKey),
        // Pressing back removes only the top entry, keeping the master pane stable.
        previousEntries = entries.dropLast(1),
        firstEntry = firstEntry,
        secondEntry = secondEntry,
      )
    } else {
      null
    }
  }
}
