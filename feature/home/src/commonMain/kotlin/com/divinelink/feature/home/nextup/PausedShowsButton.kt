package com.divinelink.feature.home.nextup

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.rememberExpandState
import com.divinelink.core.ui.resources.core_ui_collapse
import com.divinelink.core.ui.resources.core_ui_expand
import com.divinelink.core.ui.resources.on_hold
import org.jetbrains.compose.resources.stringResource

@Composable
fun PausedShowsButton(
  expanded: Boolean = rememberExpandState(ExpandableSection.ON_HOLD_SHOWS),
  onSwitchAction: (SwitchPreferencesAction) -> Unit,
) {
  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 0f else -90f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )

  TextButton(
    onClick = {
      onSwitchAction(SwitchPreferencesAction.UpdateExpandedState(ExpandableSection.ON_HOLD_SHOWS))
    },
  ) {
    Text(
      text = stringResource(UiString.on_hold),
      style = MaterialTheme.typography.titleMedium,
    )
    Icon(
      modifier = Modifier.rotate(rotationState),
      imageVector = Icons.Filled.ArrowDropDown,
      contentDescription = if (expanded) {
        stringResource(UiString.core_ui_collapse)
      } else {
        stringResource(UiString.core_ui_expand)
      },
    )
  }
}
