package com.divinelink.feature.settings.app.help

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.core.commons.DefaultBuildConfigProvider
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsExternalLinkItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.divinelink.core.commons.R as commonR

@Composable
@Destination<SettingsGraph>
fun HelpSettingsScreen(
  navigator: DestinationsNavigator,
  buildConfigProvider: BuildConfigProvider = DefaultBuildConfigProvider,
) {
  SettingsScaffold(
    title = stringResource(R.string.feature_settings_help),
    onNavigationClick = navigator::navigateUp,
  ) { paddingValues ->

    val version = UIText.ResourceText(commonR.string.version_name)

    val buildVersion = if (buildConfigProvider.isDebug) {
      UIText.StringText(version.getString() + " ${BuildConfig.BUILD_TYPE}")
    } else {
      version
    }

    ScenePeekLazyColumn(contentPadding = paddingValues) {
      item {
        SettingsTextItem(
          title = stringResource(R.string.feature_settings_help__version),
          summary = buildVersion.getString(),
        )

        SettingsExternalLinkItem(
          text = stringResource(R.string.feature_settings_help__privacy_policy),
          url = stringResource(R.string.feature_settings_help__privacy_policy_url),
        )
      }
    }
  }
}
