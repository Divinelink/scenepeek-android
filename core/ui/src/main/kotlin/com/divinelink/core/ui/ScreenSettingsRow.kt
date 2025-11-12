package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.button.switchview.SwitchViewButton

@Composable
fun ScreenSettingsRow(
  section: ViewableSection,
  onSwitchViewMode: (ViewableSection) -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End,
  ) {
    SwitchViewButton(
      section = section,
      onClick = onSwitchViewMode,
    )
  }
}
