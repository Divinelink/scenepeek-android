package com.divinelink.core.ui.components.modal.jellyseerr.manage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.core_ui_advanced
import com.divinelink.core.ui.core_ui_cancel
import com.divinelink.core.ui.core_ui_clear_data
import com.divinelink.core.ui.core_ui_clear_data_movie_description
import com.divinelink.core.ui.core_ui_clear_data_series_description
import com.divinelink.core.ui.core_ui_delete
import com.divinelink.core.ui.core_ui_delete_request
import com.divinelink.core.ui.core_ui_delete_request_confirmation_text
import com.divinelink.core.ui.core_ui_media
import com.divinelink.core.ui.core_ui_remove_from_radarr
import com.divinelink.core.ui.core_ui_remove_from_radarr_description
import com.divinelink.core.ui.core_ui_remove_from_sonarr
import com.divinelink.core.ui.core_ui_remove_from_sonarr_description
import com.divinelink.core.ui.core_ui_requests
import com.divinelink.core.ui.core_ui_season
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.extension.localizeIsoDate
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageJellyseerrMediaModal(
  requests: List<JellyseerrRequest>?,
  mediaType: MediaType,
  isLoading: Boolean,
  onDeleteRequest: (Int) -> Unit,
  onDismissRequest: () -> Unit,
  onDeleteMedia: (deleteFile: Boolean) -> Unit,
  showAdvancedOptions: Boolean,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var dialogInfo by remember { mutableStateOf<JellyseerrDialogInfo?>(null) }

  dialogInfo?.let { info ->
    DeleteRequestDialog(
      info = info,
      onDismissRequest = { dialogInfo = null },
      onConfirm = {
        when (info) {
          is JellyseerrDialogInfo.DeleteRequest -> onDeleteRequest(info.requestId)
          is JellyseerrDialogInfo.RemoveFromSonarr -> onDeleteMedia(true)
          is JellyseerrDialogInfo.RemoveFromRadarr -> onDeleteMedia(true)
          is JellyseerrDialogInfo.ClearData -> onDeleteMedia(false)
        }
        dialogInfo = null
      },
    )
  }

  ModalBottomSheet(
    modifier = Modifier
      .testTag(TestTags.Modal.BOTTOM_SHEET),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      ManageJellyseerrMediaContent(
        isLoading = isLoading,
        mediaType = mediaType,
        requests = requests,
        onDeleteRequest = { dialogInfo = JellyseerrDialogInfo.DeleteRequest(it) },
        onRemoveMedia = {
          dialogInfo = when (mediaType) {
            MediaType.MOVIE -> JellyseerrDialogInfo.RemoveFromRadarr
            MediaType.TV -> JellyseerrDialogInfo.RemoveFromSonarr
            else -> null
          }
        },
        onClearData = {
          dialogInfo = JellyseerrDialogInfo.ClearData(mediaType == MediaType.MOVIE)
        },
        showAdvancedOptions = showAdvancedOptions,
      )
    },
  )
}

@Composable
fun ManageJellyseerrMediaContent(
  isLoading: Boolean,
  mediaType: MediaType,
  requests: List<JellyseerrRequest>?,
  onDeleteRequest: (Int) -> Unit,
  onRemoveMedia: () -> Unit,
  onClearData: () -> Unit,
  showAdvancedOptions: Boolean,
) {
  LazyColumn(
    modifier = Modifier
      .testTag(TestTags.LAZY_COLUMN),
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    item {
      AnimatedVisibility(isLoading) {
        LinearProgressIndicator(
          modifier = Modifier
            .testTag(TestTags.LINEAR_LOADING_INDICATOR)
            .fillMaxWidth(),
        )
      }
    }
    if (requests?.isNotEmpty() == true) {
      item {
        Text(
          text = stringResource(UiString.core_ui_requests),
          style = MaterialTheme.typography.headlineSmall,
        )
      }
      items(
        items = requests,
        key = { it.id },
      ) { request ->
        RequestItem(
          modifier = Modifier,
          request = request,
          onDeleteRequest = onDeleteRequest,
        )
      }
      item {
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
      }
    }

    if (showAdvancedOptions) {
      item {
        MediaSection(
          mediaType = mediaType,
          onClick = onRemoveMedia,
        )
      }

      item {
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
        AdvancedSection(
          mediaType = mediaType,
          onClick = onClearData,
        )
      }
    }
  }
}

@Composable
private fun MediaSection(
  mediaType: MediaType,
  onClick: () -> Unit,
) {
  val text = when (mediaType) {
    MediaType.MOVIE -> stringResource(UiString.core_ui_remove_from_radarr)
    else -> stringResource(UiString.core_ui_remove_from_sonarr)
  }
  val description = when (mediaType) {
    MediaType.MOVIE -> stringResource(UiString.core_ui_remove_from_radarr_description)
    else -> stringResource(UiString.core_ui_remove_from_sonarr_description)
  }
  Column(
    modifier = Modifier
      .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Text(
      text = stringResource(UiString.core_ui_media),
      style = MaterialTheme.typography.headlineSmall,
    )

    Button(
      modifier = Modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colors.crimsonRed,
      ),
      onClick = onClick,
    ) {
      Text(
        text = text,
        color = Color.White,
      )
    }
    Text(
      text = "* $description",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    )
  }
}

@Composable
private fun AdvancedSection(
  mediaType: MediaType,
  onClick: () -> Unit,
) {
  val description = when (mediaType) {
    MediaType.MOVIE -> stringResource(UiString.core_ui_clear_data_movie_description)
    else -> stringResource(UiString.core_ui_clear_data_series_description)
  }
  Column(
    modifier = Modifier
      .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Text(
      text = stringResource(UiString.core_ui_advanced),
      style = MaterialTheme.typography.headlineSmall,
    )

    Button(
      modifier = Modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colors.crimsonRed,
      ),
      onClick = onClick,
    ) {
      Text(
        text = stringResource(UiString.core_ui_clear_data),
        color = Color.White,
      )
    }
    Text(
      text = "* $description",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    )
  }
}

@Composable
private fun DeleteRequestDialog(
  info: JellyseerrDialogInfo,
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.DELETE_REQUEST),
    title = { Text(info.title.getString()) },
    text = { Text(info.message.getString()) },
    onDismissRequest = onDismissRequest,
    dismissButton = {
      TextButton(
        onClick = onDismissRequest,
        content = { Text(stringResource(UiString.core_ui_cancel)) },
      )
    },
    confirmButton = {
      Button(
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colors.crimsonRed,
        ),
        onClick = onConfirm,
        content = {
          Text(
            text = stringResource(UiString.core_ui_delete),
            color = Color.White,
          )
        },
      )
    },
  )
}

@Composable
private fun LazyItemScope.RequestItem(
  modifier: Modifier = Modifier,
  request: JellyseerrRequest,
  onDeleteRequest: (Int) -> Unit,
) {
  Card(
    modifier = modifier.animateItem(),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        RequesterDisplay(request.requester)

        JellyseerrStatusPill(status = request.requestStatus)

        Row(
          modifier = Modifier,
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Icon(
            modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
          )

          Text(
            text = request.requestDate.localizeIsoDate(),
            style = MaterialTheme.typography.bodyMedium,
          )
        }

        if (request.seasons.isNotEmpty()) {
          Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            Text(
              modifier = Modifier,
              style = MaterialTheme.typography.bodyMedium,
              text = pluralStringResource(
                UiPlurals.core_ui_season,
                request.seasons.size,
                request.seasons.size,
              ),
            )

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              request.seasons.forEach {
                SeasonPill(season = it.seasonNumber)
              }
            }
          }
        }
      }

      IconButton(
        modifier = Modifier
          .testTag(TestTags.Modal.DELETE_BUTTON.format(request.id))
          .align(Alignment.Top),
        onClick = { onDeleteRequest(request.id) },
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          tint = MaterialTheme.colors.crimsonRed,
          contentDescription = null,
        )
      }
    }
  }
}

@Composable
fun SeasonPill(season: Int) {
  Text(
    modifier = Modifier
      .background(
        color = MaterialTheme.colors.vibrantPurple,
        shape = CircleShape,
      )
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_8,
        vertical = MaterialTheme.dimensions.keyline_4,
      ),
    text = season.toString(),
    style = MaterialTheme.typography.bodySmall,
    color = Color.White,
  )
}

@Composable
fun RequesterDisplay(requester: JellyseerrRequester) {
  Row(
    modifier = Modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Icon(
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
      imageVector = Icons.Default.Person,
      contentDescription = null,
    )
    Text(style = MaterialTheme.typography.bodyMedium, text = requester.displayName)
  }
}

@Composable
@Previews
fun ManageJellyseerrMediaContentAdvancedPreview() {
  PreviewLocalProvider {
    ManageJellyseerrMediaContent(
      requests = JellyseerrRequestFactory.Tv.all(),
      mediaType = MediaType.TV,
      isLoading = false,
      onDeleteRequest = {},
      onRemoveMedia = {},
      onClearData = {},
      showAdvancedOptions = true,
    )
  }
}

@Composable
@Previews
fun ManageJellyseerrMediaContentLoadingPreview() {
  PreviewLocalProvider {
    ManageJellyseerrMediaContent(
      requests = listOf(JellyseerrRequestFactory.movie()),
      mediaType = MediaType.MOVIE,
      isLoading = true,
      onDeleteRequest = {},
      onRemoveMedia = {},
      onClearData = {},
      showAdvancedOptions = true,
    )
  }
}

@Composable
@Previews
fun ManageJellyseerrMediaContentPreview() {
  PreviewLocalProvider {
    ManageJellyseerrMediaContent(
      requests = listOf(JellyseerrRequestFactory.movie()),
      mediaType = MediaType.MOVIE,
      isLoading = false,
      onDeleteRequest = {},
      onRemoveMedia = {},
      onClearData = {},
      showAdvancedOptions = false,
    )
  }
}

sealed class JellyseerrDialogInfo(
  val title: UIText,
  val message: UIText,
) {
  data class DeleteRequest(val requestId: Int) :
    JellyseerrDialogInfo(
      title = UIText.ResourceText(UiString.core_ui_delete_request),
      message = UIText.ResourceText(UiString.core_ui_delete_request_confirmation_text),
    )

  data object RemoveFromSonarr : JellyseerrDialogInfo(
    title = UIText.ResourceText(UiString.core_ui_remove_from_sonarr),
    message = UIText.ResourceText(UiString.core_ui_remove_from_sonarr_description),
  )

  data object RemoveFromRadarr : JellyseerrDialogInfo(
    title = UIText.ResourceText(UiString.core_ui_remove_from_radarr),
    message = UIText.ResourceText(UiString.core_ui_remove_from_radarr_description),
  )

  data class ClearData(val isMovie: Boolean) :
    JellyseerrDialogInfo(
      title = UIText.ResourceText(UiString.core_ui_clear_data),
      message = if (isMovie) {
        UIText.ResourceText(UiString.core_ui_clear_data_movie_description)
      } else {
        UIText.ResourceText(UiString.core_ui_clear_data_series_description)
      },
    )
}
