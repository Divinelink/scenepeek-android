package com.divinelink.feature.settings.app.account.jellyseerr.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.rememberConfigProvider
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.seerr_login_info_placeholder_pt1
import com.divinelink.feature.settings.resources.seerr_login_info_placeholder_pt2
import com.divinelink.feature.settings.resources.seerr_support_email_body
import com.divinelink.feature.settings.resources.seerr_support_email_subject
import com.divinelink.feature.settings.resources.support_email
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailSupportField() {
  val intentManager = LocalIntentManager.current
  val configProvider = rememberConfigProvider()

  val email = stringResource(Res.string.support_email)
  val subject = stringResource(Res.string.seerr_support_email_subject)
  val body = stringResource(
    Res.string.seerr_support_email_body,
    configProvider.versionData,
  )

  Row(
    modifier = Modifier
      .testTag(TestTags.Settings.Jellyseerr.EMAIL_SUPPORT)
      .clip(MaterialTheme.shapes.medium)
      .clickable {
        intentManager.launchEmail(
          email = email,
          subject = subject,
          body = body,
        )
      },
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Outlined.Info,
      contentDescription = null,
    )
    Text(
      style = MaterialTheme.typography.labelMedium,
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.Center,
      text = buildAnnotatedString {
        append(stringResource(Res.string.seerr_login_info_placeholder_pt1))
        withStyle(
          style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
          ),
        ) {
          append(email)
        }
        append(stringResource(Res.string.seerr_login_info_placeholder_pt2))
      },
    )
  }
}
