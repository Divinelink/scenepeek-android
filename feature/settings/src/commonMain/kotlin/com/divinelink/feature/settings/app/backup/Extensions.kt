package com.divinelink.feature.settings.app.backup

import androidx.compose.runtime.Composable
import com.divinelink.core.model.resources.saved
import com.divinelink.core.model.track.backup.RestoreProcess
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.episodes
import com.divinelink.core.ui.resources.restore_header
import com.divinelink.core.ui.resources.seasons
import com.divinelink.core.ui.resources.tv_shows
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.model.resources.Res as ModelRes

@Composable
fun RestoreProcess.toRestoreMessage(): String {
  val items = listOfNotNull(
    episodes.takeIf { it > 0 }?.let {
      " • $it ${stringResource(UiString.episodes)}"
    },
    seasons.takeIf { it > 0 }?.let {
      " • $it ${stringResource(UiString.seasons)}"
    },
    shows.takeIf { it > 0 }?.let {
      " • $it ${stringResource(UiString.tv_shows)}"
    },
    saved.takeIf { it > 0 }?.let {
      " • $it ${stringResource(ModelRes.string.saved)}"
    },
  )

  return buildString {
    append(stringResource(UiString.restore_header))
    if (items.isNotEmpty()) {
      append("\n\n")
      append(items.joinToString("\n"))
    }
  }
}
