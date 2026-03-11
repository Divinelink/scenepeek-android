package com.divinelink.feature.settings.app.updates

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.extensions.epochSecondsToLocalDateTime
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.extension.localizeIsoDate
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.app_updates
import com.divinelink.feature.settings.resources.check_for_updates
import com.divinelink.feature.settings.resources.last_checked
import com.divinelink.feature.settings.resources.version
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppUpdatesScreen(
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: AppUpdatesViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val lastChecked = remember(uiState.appVersion) {
    uiState.appVersion?.lastCheck?.epochSecondsToLocalDateTime()
  }

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.app_updates),
    onNavigationClick = { onNavigate.invoke(Navigation.Back) },
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier,
    ) {
      item {
        SettingsTextItem(
          title = stringResource(Res.string.version),
          summary = uiState.appVersion?.currentVersion ?: uiState.fallbackVersion,
        )
      }

      item {
        SettingsClickItem(
          text = stringResource(Res.string.check_for_updates),
          summary = lastChecked?.let {
            stringResource(
              Res.string.last_checked,
              lastChecked.localizeIsoDate(useLong = false) + " ${lastChecked.time}",
            )
          },
          onClick = viewModel::checkForUpdates,
        )
      }
    }
  }
}
