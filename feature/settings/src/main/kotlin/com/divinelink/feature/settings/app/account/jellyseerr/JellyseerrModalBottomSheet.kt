package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.jellyseerr.JellyseerrIntegration
import com.divinelink.core.ui.Previews

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JellyseerrModalBottomSheet(
  jellyseerrIntegration: JellyseerrIntegration,
  onDismissRequest: () -> Unit,
  onApiKeyChange: (String) -> Unit,
  onAddressChange: (String) -> Unit,
  onTestClick: () -> Unit,
  onSaveClick: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
  )

  ModalBottomSheet(
    sheetState = sheetState,
    windowInsets = WindowInsets.ime,
    onDismissRequest = onDismissRequest,
  ) {
    JellyseerrBottomSheetContent(
      jellyseerrIntegration = jellyseerrIntegration,
      onTestClick = onTestClick,
      onSaveClick = onSaveClick,
      onApiKeyChange = onApiKeyChange,
      onAddressChange = onAddressChange,
    )
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}

@Previews
@Composable
private fun JellyseerrModalBottomSheetPreview() {
  AppTheme {
    Surface {
      JellyseerrModalBottomSheet(
        jellyseerrIntegration = JellyseerrIntegration(
          address = "address",
          apiKey = "apiKey",
        ),
        onDismissRequest = { },
        onTestClick = {},
        onSaveClick = {},
        onApiKeyChange = {},
        onAddressChange = {},
      )
    }
  }
}
