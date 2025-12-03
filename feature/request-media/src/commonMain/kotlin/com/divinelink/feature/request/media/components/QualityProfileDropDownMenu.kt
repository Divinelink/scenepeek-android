package com.divinelink.feature.request.media.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.resources.Res
import com.divinelink.feature.request.media.resources.feature_request_media_default
import com.divinelink.feature.request.media.resources.feature_request_media_quality_profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun QualityProfileDropDownMenu(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  options: List<InstanceProfile>,
  currentInstance: LCEState<InstanceProfile>,
  onUpdate: (InstanceProfile) -> Unit,
) {
  LceDropdownMenu(
    modifier = modifier,
    options = options,
    enabled = enabled,
    currentInstance = currentInstance,
    label = { Text(stringResource(Res.string.feature_request_media_quality_profile)) },
    displayText = { profile ->
      buildString {
        append(profile.name)
        if (profile.isDefault) {
          append(" (${stringResource(Res.string.feature_request_media_default)})")
        }
      }
    },
    onUpdate = onUpdate,
  )
}
