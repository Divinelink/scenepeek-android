package com.divinelink.feature.request.media.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.LceDropdownMenu
import com.divinelink.feature.request.media.R

@Composable
fun DestinationServerDropDownMenu(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  options: List<SonarrInstance>,
  currentInstance: LCEState<SonarrInstance>,
  onUpdate: (SonarrInstance) -> Unit,
) {
  LceDropdownMenu(
    modifier = modifier,
    enabled = enabled,
    options = options,
    currentInstance = currentInstance,
    label = { Text(stringResource(R.string.feature_request_media_destination_server)) },
    displayText = { it.name },
    onUpdate = onUpdate,
  )
}
