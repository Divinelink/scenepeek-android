package com.divinelink.core.ui.components.modal.jellyseerr.manage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.JellyseerrStatusPill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageJellyseerrMediaModal(
  requests: List<JellyseerrRequest>?,
  isLoading: Boolean,
  onDeleteRequest: (Int) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var deleteRequestId: Int? by remember { mutableStateOf(null) }

  if (deleteRequestId != null) {
    DeleteRequestDialog(
      onDismissRequest = { deleteRequestId = null },
      onConfirm = {
        deleteRequestId?.let(onDeleteRequest)
        deleteRequestId = null
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
        requests = requests,
        onDeleteRequest = { deleteRequestId = it },
      )
    },
  )
}

@Composable
fun ManageJellyseerrMediaContent(
  isLoading: Boolean,
  requests: List<JellyseerrRequest>?,
  onDeleteRequest: (Int) -> Unit,
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
          text = stringResource(R.string.core_ui_requests),
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
    }
  }
}

@Composable
private fun DeleteRequestDialog(
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.DELETE_REQUEST),
    title = { Text(stringResource(id = R.string.core_ui_delete_request_title)) },
    text = { Text(stringResource(id = R.string.core_ui_delete_request_confirmation_text)) },
    onDismissRequest = onDismissRequest,
    dismissButton = {
      ElevatedButton(
        onClick = onDismissRequest,
        content = { Text(stringResource(id = R.string.core_ui_cancel)) },
      )
    },
    confirmButton = {
      Button(
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colors.crimsonRed,
        ),
        onClick = { onConfirm() },
        content = { Text(stringResource(id = R.string.core_ui_delete)) },
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

        JellyseerrStatusPill(status = request.status)

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
            text = request.requestDate,
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
                id = R.plurals.core_ui_season,
                request.seasons.size,
                request.seasons.size,
              ),
            )

            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              request.seasons.forEach {
                SeasonPill(season = it)
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
private fun SeasonPill(season: Int) {
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
private fun RequesterDisplay(requester: JellyseerrRequester) {
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
fun ManageJellyseerrMediaContentPreview() {
  AppTheme {
    Surface {
      ManageJellyseerrMediaContent(
        requests = JellyseerrRequestFactory.Tv.all(),
        isLoading = false,
        onDeleteRequest = {},
      )
    }
  }
}

@Composable
@Preview
fun ManageJellyseerrMediaContentLoadingPreview() {
  AppTheme {
    Surface {
      ManageJellyseerrMediaContent(
        requests = listOf(JellyseerrRequestFactory.movie()),
        isLoading = true,
        onDeleteRequest = {},
      )
    }
  }
}
