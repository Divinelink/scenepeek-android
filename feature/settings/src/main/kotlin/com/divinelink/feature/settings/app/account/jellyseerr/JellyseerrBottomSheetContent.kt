package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrIntegration
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrBottomSheetContent(
  jellyseerrIntegration: JellyseerrIntegration,
  onApiKeyChange: (String) -> Unit,
  onAddressChange: (String) -> Unit,
  onTestClick: () -> Unit,
  onSaveClick: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(R.string.feature_settings_jellyseerr_description),
    )

    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = jellyseerrIntegration.address,
      onValueChange = { onAddressChange(it) },
      label = { Text(text = stringResource(R.string.feature_settings_address)) },
    )

    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = jellyseerrIntegration.apiKey,
      onValueChange = { onApiKeyChange(it) },
      label = { Text(text = stringResource(R.string.feature_settings_api_key)) },
    )

    Spacer(Modifier.height(MaterialTheme.dimensions.keyline_32))

    OutlinedButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = onTestClick,
    ) {
      Text(stringResource(id = uiR.string.core_ui_test))
    }

    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = onSaveClick,
    ) {
      Text(stringResource(id = uiR.string.core_ui_save))
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AppTheme {
    Surface {
      JellyseerrBottomSheetContent(
        jellyseerrIntegration = JellyseerrIntegration(
          address = "http://localhost:8080",
          apiKey = "apiKey",
        ),
        onTestClick = {},
        onSaveClick = {},
        onAddressChange = {},
        onApiKeyChange = {},
      )
    }
  }
}
