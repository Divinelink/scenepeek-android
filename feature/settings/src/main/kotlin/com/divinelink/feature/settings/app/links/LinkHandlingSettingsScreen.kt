package com.divinelink.feature.settings.app.links

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.divinelink.core.commons.util.AppSettingsUtil
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.core.commons.R as CoreR

@Composable
fun LinkHandlingSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateUp: () -> Unit,
) {
  val context = LocalContext.current

  val directions = buildAnnotatedString {
    append(stringResource(id = R.string.feature_settings_enable_link_handling_step_1))
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
      append(stringResource(id = R.string.feature_settings_enable_link_handling_step_2))
    }
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
      append(stringResource(id = R.string.feature_settings_enable_link_handling_step_3))
    }
    append(stringResource(id = R.string.feature_settings_enable_link_handling_step_4))
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
      append(
        stringResource(
          id = R.string.feature_settings_enable_link_handling_step_5,
          stringResource(CoreR.string.core_commons_app_name),
        ),
      )
    }
    append(
      stringResource(
        id = R.string.feature_settings_enable_link_handling_step_6,
        stringResource(CoreR.string.core_commons_app_name),
      ),
    )
  }

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(id = R.string.feature_settings_link_handling),
    floatingActionButton = {
      ScaffoldFab(
        icon = Icons.Default.Settings,
        text = stringResource(id = R.string.feature_settings_open_settings),
        expanded = true,
        onClick = { AppSettingsUtil.openAppDetails(context) },
      )
    },
    onNavigationClick = navigateUp,
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.LAZY_COLUMN),
    ) {
      item {
        SettingsTextItem(
          title = stringResource(
            id = R.string.feature_settings_link_handling_dialog_title,
            stringResource(CoreR.string.core_commons_app_name),
          ),
          summary = stringResource(
            id = R.string.feature_settings_link_handling_dialog_summary,
            stringResource(CoreR.string.core_commons_app_name),
          ),
        )
      }

      item {
        Text(
          modifier = Modifier
            .testTag(TestTags.Settings.LinkHandling.DIRECTIONS_TEXT)
            .padding(MaterialTheme.dimensions.keyline_16),
          text = directions,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.secondary,
        )
      }
    }
  }
}
