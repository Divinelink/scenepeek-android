package com.divinelink.feature.requests.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.PreviewLocalProvider

object ActionButton {

  @Composable
  fun Approve(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier,
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_approve),
      vector = Icons.Default.Done,
      color = MaterialTheme.colors.emeraldGreen,
    )
  }

  @Composable
  fun Decline(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier,
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_decline),
      vector = Icons.Default.Close,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  fun EditRequest(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_edit_request),
      vector = Icons.Default.Edit,
      color = MaterialTheme.colors.vibrantPurple,
    )
  }

  @Composable
  fun Retry(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_retry),
      vector = Icons.Default.Autorenew,
      color = MaterialTheme.colors.vibrantPurple,
    )
  }

  @Composable
  fun CancelRequest(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_cancel_request),
      vector = Icons.Default.Close,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  fun DeleteRequest(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_delete_request),
      vector = Icons.Default.Delete,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  fun RemoveFromServer(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    mediaType: MediaType,
    onClick: () -> Unit,
  ) {
    if (mediaType == MediaType.TV) {
      RemoveFromSonarr(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
      )
    } else {
      RemoveFromRadarr(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
      )
    }
  }

  @Composable
  private fun RemoveFromSonarr(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_remove_from_sonarr),
      vector = Icons.Default.Delete,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  private fun RemoveFromRadarr(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    ActionButton(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_remove_from_radarr),
      vector = Icons.Default.Delete,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  private fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    vector: ImageVector,
    color: Color,
  ) {
    Button(
      modifier = modifier,
      enabled = enabled,
      colors = ButtonDefaults.buttonColors().copy(
        containerColor = color,
      ),
      onClick = onClick,
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = vector,
          contentDescription = null,
          tint = Color.White,
        )

        Text(text = text, color = Color.White)
      }
    }
  }
}

@Previews
@Composable
fun ActionButtonPreviews() {
  PreviewLocalProvider {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      ActionButton.Approve(enabled = true) { }
      ActionButton.Approve(enabled = false) { }

      ActionButton.Decline(enabled = true) { }
      ActionButton.Decline(enabled = false) { }

      ActionButton.Retry(enabled = true) { }
      ActionButton.Retry(enabled = false) { }

      ActionButton.EditRequest(enabled = true) { }
      ActionButton.EditRequest(enabled = false) { }

      ActionButton.DeleteRequest(enabled = true) { }
      ActionButton.DeleteRequest(enabled = false) { }

      ActionButton.RemoveFromServer(enabled = true, mediaType = MediaType.TV) { }
      ActionButton.RemoveFromServer(enabled = false, mediaType = MediaType.TV) { }

      ActionButton.RemoveFromServer(enabled = true, mediaType = MediaType.MOVIE) { }
      ActionButton.RemoveFromServer(enabled = false, mediaType = MediaType.MOVIE) { }
    }
  }
}
