package com.divinelink.feature.settings.app.about

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.core_commons_app_name
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.components.SettingsExternalLinkItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.feature_settings_about
import com.divinelink.feature.settings.feature_settings_about__privacy_policy
import com.divinelink.feature.settings.feature_settings_about__privacy_policy_url
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun AboutSettingsScreen(
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  buildConfigProvider: BuildConfigProvider = koinInject(),
) {
  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.feature_settings_about),
    onNavigationClick = { onNavigate.invoke(Navigation.Back) },
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.Settings.About.SCROLLABLE_CONTENT),
    ) {
      item {
        AboutCard(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          name = stringResource(com.divinelink.core.commons.Res.string.core_commons_app_name),
          version = buildConfigProvider.versionName.lowercase(),
          github = "Divinelink",
          onNavigate = onNavigate,
        )
      }
      item {
        SettingsExternalLinkItem(
          onNavigate = onNavigate,
          text = stringResource(Res.string.feature_settings_about__privacy_policy),
          url = stringResource(Res.string.feature_settings_about__privacy_policy_url),
        )
      }
    }
  }
}
