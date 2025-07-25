package com.divinelink.feature.lists.create.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.create.CreateListAction
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.create.ui.provider.CreateListUiStateParameterProvider
import com.divinelink.core.ui.R as uiR

@Composable
fun CreateListContent(
  uiState: CreateListUiState,
  action: (CreateListAction) -> Unit,
) {
  ScenePeekLazyColumn(
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_32),
  ) {
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
  }
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
