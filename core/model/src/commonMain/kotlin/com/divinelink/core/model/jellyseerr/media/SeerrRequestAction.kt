package com.divinelink.core.model.jellyseerr.media

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SeerrRequestAction(
  val icon: ImageVector,
) {
  data object Approve : SeerrRequestAction(
    icon = Icons.Default.Done,
  )

  data object Decline : SeerrRequestAction(
    icon = Icons.Default.Close,
  )

  data object Edit : SeerrRequestAction(
    icon = Icons.Default.Edit,
  )

  data object Retry : SeerrRequestAction(
    icon = Icons.Default.Autorenew,
  )

  data object Cancel : SeerrRequestAction(
    icon = Icons.Default.Close,
  )

  data object Delete : SeerrRequestAction(
    icon = Icons.Default.Delete,
  )

  data object RemoveFromSonarr : SeerrRequestAction(
    icon = Icons.Default.Delete,
  )

  data object RemoveFromRadarr : SeerrRequestAction(
    icon = Icons.Default.Delete,
  )

  companion object {
    val entries = listOf(
      Approve,
      Decline,
      Edit,
      Retry,
      Cancel,
      Delete,
      RemoveFromSonarr,
      RemoveFromRadarr,
    )
  }
}
