package com.divinelink.feature.request.media.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.Res
import com.divinelink.feature.request.media.feature_request_media_default
import com.divinelink.feature.request.media.feature_request_media_root_folder
import org.jetbrains.compose.resources.stringResource

@Composable
fun RootFolderDropDownMenu(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  options: List<InstanceRootFolder>,
  currentInstance: LCEState<InstanceRootFolder>,
  onUpdate: (InstanceRootFolder) -> Unit,
) {
  LceDropdownMenu(
    modifier = modifier,
    options = options,
    enabled = enabled,
    currentInstance = currentInstance,
    label = { Text(stringResource(Res.string.feature_request_media_root_folder)) },
    displayText = { folder ->
      buildString {
        append(folder.path)
        append(" (${folder.freeSpace})")
        if (folder.isDefault) {
          append(" (${stringResource(Res.string.feature_request_media_default)})")
        }
      }
    },
    onUpdate = onUpdate,
  )
}
