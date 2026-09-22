package com.divinelink.feature.settings.app.backup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.DateFormatStyle
import com.divinelink.core.commons.formatLocalized
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.dialog.BasicDialog
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.resources.restore_completed
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsSwitchItem
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.automatic_backups_disabled
import com.divinelink.feature.settings.resources.automatic_backups_subtitle
import com.divinelink.feature.settings.resources.automatic_backups_title
import com.divinelink.feature.settings.resources.backup
import com.divinelink.feature.settings.resources.backup_and_restore
import com.divinelink.feature.settings.resources.backup_location_subtitle
import com.divinelink.feature.settings.resources.backup_location_title
import com.divinelink.feature.settings.resources.backup_summary
import com.divinelink.feature.settings.resources.last_backup_text
import com.divinelink.feature.settings.resources.restore
import com.divinelink.feature.settings.resources.restore_summary
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimatedVisibilityScope.BackupScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: BackupViewModel = koinViewModel(),
) {
  val scope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  uiState.dialog?.let { state ->
    BasicDialog(
      dialogState = state,
      onDismissRequest = { viewModel.setDialog(null) },
    )
  }

  uiState.restoreProcess?.let { process ->
    BasicDialog(
      dialogState = DialogState(
        title = UIText.ResourceText(UiString.restore_completed),
        message = UIText.StringText(process.toRestoreMessage()),
        error = null,
      ),
      onDismissRequest = { viewModel.setDialog(null) },
    )
  }

  SettingsScaffold(
    animatedVisibilityScope = this,
    title = stringResource(Res.string.backup_and_restore),
    onNavigationClick = { onNavigate.invoke(Navigation.Back) },
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier,
    ) {
      item {
        AnimatedVisibility(
          visible = uiState.loading,
          modifier = Modifier.fillMaxWidth(),
        ) {
          LinearProgressIndicator(
            modifier = Modifier
              .testTag(TestTags.LINEAR_LOADING_INDICATOR)
              .fillMaxWidth(),
          )
        }
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Backup),
          text = stringResource(Res.string.backup),
          summary = stringResource(Res.string.backup_summary),
          onClick = { viewModel.backupLibrary() },
        )
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.SettingsBackupRestore),
          text = stringResource(Res.string.restore),
          summary = stringResource(Res.string.restore_summary),
          onClick = {
            scope.launch { viewModel.restore() }
          },
        )
      }

      item {
        HorizontalDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Folder),
          text = stringResource(Res.string.backup_location_title),
          summary = stringResource(Res.string.backup_location_subtitle),
          onClick = { viewModel.onAction(BackupAction.SetBackupLocation) },
        )
      }

      item {
        SettingsSwitchItem(
          icon = IconWrapper.Vector(Icons.Outlined.CalendarToday),
          title = stringResource(Res.string.automatic_backups_title),
          summary = stringResource(Res.string.automatic_backups_subtitle),
          enabled = uiState.backupInfo.pathBackup != null,
          onCheckedChange = { viewModel.onAction(BackupAction.UpdateAutomaticBackups(it)) },
          isChecked = uiState.backupInfo.automaticBackups,
        )
      }

      if (uiState.backupInfo.pathBackup == null) {
        item {
          Row(
            modifier = Modifier.padding(
              horizontal = MaterialTheme.dimensions.keyline_16,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Icon(
              imageVector = Icons.Outlined.Info,
              tint = MaterialTheme.colorScheme.error,
              contentDescription = null,
            )
            Text(
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall,
              text = stringResource(Res.string.automatic_backups_disabled),
            )
          }
        }
      }

      item {
        uiState.backupInfo.lastBackupAt?.formatLocalized(DateFormatStyle.LONG)
          ?.let { lastBackup ->
            Text(
              modifier = Modifier.padding(
                vertical = MaterialTheme.dimensions.keyline_16,
                horizontal = MaterialTheme.dimensions.keyline_16,
              ),
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.bodySmall,
              text = stringResource(Res.string.last_backup_text, lastBackup),
            )
          }
      }
    }
  }
}
