package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
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
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.Previews

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JellyseerrModalBottomSheet(
  jellyseerrState: JellyseerrState,
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
    AnimatedContent(
      targetState = jellyseerrState,
      label = "Jellyseerr State Animated Content",
      contentKey = { state ->
        when (state) {
          is JellyseerrState.Initial -> "Initial"
          is JellyseerrState.LoggedIn -> "LoggedIn"
        }
      },
    ) { stateClass ->
      when (stateClass) {
        is JellyseerrState.Initial -> {
          JellyseerrBottomSheetContent(
            jellyseerrState = stateClass,
            interaction = interaction,
          )
        }
        is JellyseerrState.LoggedIn ->
          JellyseerrLoggedInBottomSheetContent(
            jellyseerrState = stateClass,
            onLogoutClock = { interaction(it) },
          )
      }
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}

@Previews
@Composable
private fun JellyseerrModalBottomSheetPreview() {
  AppTheme {
    Surface {
      JellyseerrModalBottomSheet(
        jellyseerrState = JellyseerrState.Initial(null),
        onDismissRequest = {},
        interaction = {},
      )
    }
  }
}
