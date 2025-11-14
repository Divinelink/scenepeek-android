package com.divinelink.feature.request.media.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.Res
import com.divinelink.feature.request.media.feature_request_media_default
import com.divinelink.feature.request.media.feature_request_media_destination_server
import org.jetbrains.compose.resources.stringResource

@Composable
fun DestinationServerDropDownMenu(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  options: List<ServerInstance>,
  currentInstance: LCEState<ServerInstance>,
  onUpdate: (ServerInstance) -> Unit,
) {
  LceDropdownMenu(
    modifier = modifier,
    enabled = enabled,
    options = options,
    currentInstance = currentInstance,
    label = { Text(stringResource(Res.string.feature_request_media_destination_server)) },
    displayText = {
      buildString {
        append(it.name)
        if (it.isDefault) {
          append(" (${stringResource(Res.string.feature_request_media_default)})")
        }
      }
    },
    onUpdate = onUpdate,
  )
}
