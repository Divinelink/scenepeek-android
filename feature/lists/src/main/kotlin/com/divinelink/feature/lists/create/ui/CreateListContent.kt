package com.divinelink.feature.lists.create.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.ListItemBackdropImage
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.create.CreateListAction
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.create.backdrop.SelectBackdropModal
import com.divinelink.feature.lists.create.ui.provider.CreateListUiStateParameterProvider
import com.divinelink.core.ui.R as uiR

@Composable
fun CreateListContent(
  uiState: CreateListUiState,
  action: (CreateListAction) -> Unit,
) {
  var deleteDialog by rememberSaveable { mutableStateOf(false) }
  var showBackdropModal by rememberSaveable { mutableStateOf(false) }

  if (deleteDialog) {
    DeleteListDialog(
      onDismissRequest = { deleteDialog = false },
      onConfirm = {
        action.invoke(CreateListAction.DeleteList)
        deleteDialog = false
      },
    )
  }

  if (showBackdropModal) {
    SelectBackdropModal(
      id = uiState.id,
      onDismissRequest = { showBackdropModal = false },
      onBackdropSelected = { backdrop ->
        action.invoke(CreateListAction.BackdropChanged(backdrop))
        showBackdropModal = false
      },
    )
  }

  AnimatedVisibility(
    visible = uiState.loading,
    modifier = Modifier.fillMaxWidth(),
  ) {
    LinearProgressIndicator(
      modifier = Modifier
        .testTag(TestTags.LINEAR_LOADING_INDICATOR)
        .fillMaxWidth(),
    )
  }

  ScenePeekLazyColumn(
    modifier = Modifier.testTag(
      TestTags.Components.SCROLLABLE_CONTENT,
    ),
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_32),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    if (uiState.editMode) {
      item {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          ListItemBackdropImage(
            modifier = Modifier
              .width(160.dp)
              .clickable(
                onClickLabel = stringResource(uiR.string.core_ui_select_media_backdrop_image),
                onClick = {
                  showBackdropModal = true
                },
              ),
            url = uiState.backdrop,
          )

          TextButton(
            onClick = {
              showBackdropModal = true
            },
          ) {
            Text(stringResource(R.string.feature_lists_change_image))
          }
        }
      }
    }

    item {
      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next,
        ),
        value = uiState.name,
        singleLine = true,
        onValueChange = { action.invoke(CreateListAction.NameChanged(it)) },
        label = { Text(text = stringResource(R.string.feature_lists_name)) },
      )
    }

    item {
      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next,
        ),
        value = uiState.description,
        singleLine = false,
        onValueChange = { action.invoke(CreateListAction.DescriptionChanged(it)) },
        label = { Text(text = stringResource(R.string.feature_lists_description)) },
      )
    }

    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(
            interactionSource = null,
            indication = null,
          ) {
            action.invoke(CreateListAction.PublicChanged)
          },
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(0.8f),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            text = stringResource(uiR.string.core_ui_public),
            style = MaterialTheme.typography.titleSmall,
          )
          Text(
            text = stringResource(R.string.feature_lists_public_description),
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        Switch(
          checked = uiState.public,
          onCheckedChange = { action.invoke(CreateListAction.PublicChanged) },
        )
      }
    }

    if (uiState.editMode) {
      item {
        TextButton(
          onClick = { deleteDialog = true },
        ) {
          Text(
            text = stringResource(R.string.feature_lists_delete_list),
            color = MaterialTheme.colors.crimsonRed,
          )
        }
      }
    }
  }
}

@Composable
private fun DeleteListDialog(
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.DELETE_REQUEST),
    title = { Text(stringResource(R.string.feature_lists_delete_list)) },
    text = { Text(stringResource(R.string.feature_lists_delete_list_alert_message)) },
    onDismissRequest = onDismissRequest,
    dismissButton = {
      TextButton(
        onClick = onDismissRequest,
        content = { Text(stringResource(id = com.divinelink.core.ui.R.string.core_ui_cancel)) },
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
            text = stringResource(id = com.divinelink.core.ui.R.string.core_ui_delete),
            color = Color.White,
          )
        },
      )
    },
  )
}

@Composable
@Previews
fun CreateListContentPreview(
  @PreviewParameter(CreateListUiStateParameterProvider::class) state: CreateListUiState,
) {
  AppTheme {
    Surface {
      CreateListContent(
        uiState = state,
        action = { },
      )
    }
  }
}
