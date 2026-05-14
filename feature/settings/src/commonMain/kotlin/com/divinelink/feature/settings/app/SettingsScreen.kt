package com.divinelink.feature.settings.app

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Groups2
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.platform.Platform
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import com.divinelink.core.ui.rememberCurrentPlatform
import com.divinelink.feature.settings.components.SettingsClickItem
import com.divinelink.feature.settings.components.SettingsDivider
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.modal.SettingsModalContent
import com.divinelink.feature.settings.modal.SettingsModalOption
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.app_updates
import com.divinelink.feature.settings.resources.feature_settings_about
import com.divinelink.feature.settings.resources.feature_settings_details_preferences
import com.divinelink.feature.settings.resources.feature_settings_help_with_translation_summary
import com.divinelink.feature.settings.resources.feature_settings_help_with_translation_title
import com.divinelink.feature.settings.resources.feature_settings_join_the_community_summary
import com.divinelink.feature.settings.resources.feature_settings_join_the_community_title
import com.divinelink.feature.settings.resources.feature_settings_link_handling
import com.divinelink.feature.settings.resources.feature_settings_rate_the_application_summary
import com.divinelink.feature.settings.resources.feature_settings_rate_the_application_title
import com.divinelink.feature.settings.resources.feature_settings_send_feedback_summary
import com.divinelink.feature.settings.resources.feature_settings_send_feedback_title
import com.divinelink.feature.settings.resources.matrix_url
import com.divinelink.feature.settings.resources.preferences__account
import com.divinelink.feature.settings.resources.preferences__appearance
import com.divinelink.feature.settings.resources.settings
import com.divinelink.feature.settings.resources.weblate_url
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
) {
  val currentPlatform = rememberCurrentPlatform()
  val urlHandler = rememberUrlHandler()
  var modalOptions by remember { mutableStateOf<List<SettingsModalOption>?>(null) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  modalOptions?.let { options ->
    ModalBottomSheet(
      sheetState = sheetState,
      onDismissRequest = { modalOptions = null },
    ) {
      SettingsModalContent(
        options = options,
        urlHandler = urlHandler,
      )
    }
  }

  SettingsScaffold(
    title = stringResource(Res.string.settings),
    onNavigationClick = { onNavigate(Navigation.Back) },
    animatedVisibilityScope = animatedVisibilityScope,
    navigationIconPainter = Icons.AutoMirrored.Rounded.ArrowBack,
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.Settings.SCREEN_CONTENT),
    ) {
      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.AccountCircle),
          text = stringResource(Res.string.preferences__account),
          onClick = { onNavigate(Navigation.AccountSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Palette),
          text = stringResource(Res.string.preferences__appearance),
          onClick = { onNavigate(Navigation.AppearanceSettingsRoute) },
        )
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.DisplaySettings),
          text = stringResource(Res.string.feature_settings_details_preferences),
          onClick = { onNavigate(Navigation.DetailsPreferencesSettingsRoute) },
        )
      }

      if (currentPlatform == Platform.Android) {
        item {
          SettingsClickItem(
            icon = IconWrapper.Vector(Icons.Outlined.Link),
            text = stringResource(Res.string.feature_settings_link_handling),
            onClick = { onNavigate(Navigation.LinkHandlingSettingsRoute) },
          )
        }
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Update),
          text = stringResource(Res.string.app_updates),
          onClick = { onNavigate(Navigation.AppUpdatesSettingsRoute) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        val url = stringResource(Res.string.matrix_url)

        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Groups2),
          text = stringResource(Res.string.feature_settings_join_the_community_title),
          summary = stringResource(Res.string.feature_settings_join_the_community_summary),
          onClick = { urlHandler.openUrl(url) },
        )
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Rounded.Star),
          text = stringResource(Res.string.feature_settings_rate_the_application_title),
          summary = stringResource(Res.string.feature_settings_rate_the_application_summary),
          onClick = {
            modalOptions = listOf(
              SettingsModalOption.GitHubStar,
              SettingsModalOption.RateGooglePlay,
            )
          },
        )
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Feedback),
          text = stringResource(Res.string.feature_settings_send_feedback_title),
          summary = stringResource(Res.string.feature_settings_send_feedback_summary),
          onClick = {
            modalOptions = listOf(
              SettingsModalOption.OpenGithubIssue,
              SettingsModalOption.SendEmail,
            )
          },
        )
      }

      item {
        val url = stringResource(Res.string.weblate_url)

        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.Outlined.Translate),
          text = stringResource(Res.string.feature_settings_help_with_translation_title),
          summary = stringResource(Res.string.feature_settings_help_with_translation_summary),
          onClick = { urlHandler.openUrl(url) },
        )
      }

      item {
        SettingsDivider()
      }

      item {
        SettingsClickItem(
          icon = IconWrapper.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
          text = stringResource(Res.string.feature_settings_about),
          onClick = { onNavigate(Navigation.AboutSettingsRoute) },
        )
      }
    }
  }
}
