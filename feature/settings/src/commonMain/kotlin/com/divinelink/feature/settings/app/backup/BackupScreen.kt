package com.divinelink.feature.settings.app.backup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.dialog.BasicDialog
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.resources.backup_completed
import com.divinelink.core.ui.resources.backup_failed
import com.divinelink.core.ui.resources.restore_completed
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.backup
import com.divinelink.feature.settings.resources.backup_and_restore
import com.divinelink.feature.settings.resources.restore
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimatedVisibilityScope.BackupScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: BackupViewModel = koinViewModel(),
) {
  val intentManager = LocalIntentManager.current
  val scope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.exportFile.collect { file ->
      intentManager
        .saveFile(file)
        .fold(
          onSuccess = { saved ->
            if (saved) {
              viewModel.setDialog(
                dialog = DialogState(
                  message = UIText.ResourceText(UiString.backup_completed),
                  error = null,
                ),
              )
            }
          },
          onFailure = {
            viewModel.setDialog(
              dialog = DialogState(
                message = UIText.ResourceText(UiString.backup_failed),
                error = null,
              ),
            )
          },
        )
    }
  }

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
          icon = IconWrapper.Vector(Icons.Default.Backup),
          text = stringResource(Res.string.backup),
          onClick = { viewModel.backupLibrary() },
        )
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Default.Restore),
          text = stringResource(Res.string.restore),
          onClick = {
            scope.launch {
              viewModel.restore(intentManager.readFile(setOf("json")))
            }
          },
        )
      }
    }
  }
}
