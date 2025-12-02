package com.divinelink.core.ui.button.action

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.core_ui_approve
import com.divinelink.core.ui.core_ui_cancel_request
import com.divinelink.core.ui.core_ui_decline
import com.divinelink.core.ui.core_ui_delete_request
import com.divinelink.core.ui.core_ui_edit_request
import com.divinelink.core.ui.core_ui_remove_from_radarr
import com.divinelink.core.ui.core_ui_remove_from_sonarr
import com.divinelink.core.ui.core_ui_request
import com.divinelink.core.ui.core_ui_request_series_button
import com.divinelink.core.ui.core_ui_retry
import com.divinelink.core.ui.core_ui_select_seasons_button
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

object ActionButton {

  @Composable
  fun Approve(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    Button(
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
    Button(
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
    Button(
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
    Button(
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
    Button(
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
    Button(
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
  fun RequestTVShow(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    selectedSeasons: Int,
    onClick: () -> Unit,
  ) {
    val text = if (selectedSeasons == 0) {
      stringResource(UiString.core_ui_select_seasons_button)
    } else {
      pluralStringResource(
        UiPlurals.core_ui_request_series_button,
        selectedSeasons,
        selectedSeasons,
      )
    }

    Button(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled && selectedSeasons != 0,
      text = text,
      vector = Icons.Default.Download,
      color = MaterialTheme.colors.vibrantPurple,
    )
  }

  @Composable
  fun RequestMovie(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    Button(
      modifier = modifier
        .fillMaxWidth()
        .testTag(TestTags.ActionButton.REQUEST_MOVIE_BUTTON),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_request),
      vector = Icons.Default.Download,
      color = MaterialTheme.colors.vibrantPurple,
    )
  }

  @Composable
  private fun RemoveFromSonarr(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
  ) {
    Button(
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
    Button(
      modifier = modifier.fillMaxWidth(),
      onClick = onClick,
      enabled = enabled,
      text = stringResource(UiString.core_ui_remove_from_radarr),
      vector = Icons.Default.Delete,
      color = MaterialTheme.colors.crimsonRed,
    )
  }

  @Composable
  private fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    vector: ImageVector,
    color: Color,
  ) {
    val textColor by animateColorAsState(
      targetValue = when (enabled) {
        true -> Color.White
        false -> Color.White.copy(alpha = 0.6f)
      },
      label = "TextColorAnimation",
    )

    val buttonColor by animateColorAsState(
      targetValue = when (enabled) {
        true -> color
        false -> color.copy(alpha = 0.6f)
      },
      label = "ButtonColorAnimation",
    )

    Button(
      modifier = modifier,
      enabled = enabled,
      colors = ButtonDefaults.buttonColors().copy(
        containerColor = buttonColor,
      ),
      onClick = onClick,
      contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_8),
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = vector,
          contentDescription = null,
          tint = textColor,
        )

        Text(
          text = text,
          color = textColor,
          maxLines = 1,
          textAlign = TextAlign.Center,
          overflow = TextOverflow.Ellipsis,
        )
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

      ActionButton.CancelRequest(enabled = true) { }
      ActionButton.CancelRequest(enabled = false) { }

      ActionButton.DeleteRequest(enabled = true) { }
      ActionButton.DeleteRequest(enabled = false) { }
    }
  }
}

@Previews
@Composable
fun ActionButtonsPreviews() {
  PreviewLocalProvider {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      ActionButton.RemoveFromServer(enabled = true, mediaType = MediaType.TV) { }
      ActionButton.RemoveFromServer(enabled = false, mediaType = MediaType.TV) { }

      ActionButton.RemoveFromServer(enabled = true, mediaType = MediaType.MOVIE) { }
      ActionButton.RemoveFromServer(enabled = false, mediaType = MediaType.MOVIE) { }

      ActionButton.RequestTVShow(enabled = true, selectedSeasons = 0) { }
      ActionButton.RequestTVShow(enabled = true, selectedSeasons = 5) { }
      ActionButton.RequestTVShow(enabled = false, selectedSeasons = 0) { }
      ActionButton.RequestTVShow(enabled = false, selectedSeasons = 5) { }

      ActionButton.RequestMovie(enabled = true) { }
      ActionButton.RequestMovie(enabled = false) { }
    }
  }
}
