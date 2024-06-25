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
import com.divinelink.core.model.jellyseerr.JellyseerrDetails
import com.divinelink.core.ui.Previews

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JellyseerrModalBottomSheet(
  jellyseerrDetails: JellyseerrDetails?,
  onDismissRequest: () -> Unit,
  interaction: (JellyseerrInteraction) -> Unit,
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
      jellyseerrDetails = jellyseerrDetails,
      interaction = interaction,
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
        jellyseerrDetails = JellyseerrDetails(
          address = "address",
          apiKey = "apiKey",
        ),
        onDismissRequest = {},
        interaction = {},
      )
    }
  }
}
