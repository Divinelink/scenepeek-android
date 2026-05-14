package com.divinelink.feature.settings.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_about__repository_url
import com.divinelink.feature.settings.resources.google_play_url
import com.divinelink.feature.settings.resources.raise_an_issue_on_github
import com.divinelink.feature.settings.resources.raise_github_issue_url
import com.divinelink.feature.settings.resources.rate_on_google_play
import com.divinelink.feature.settings.resources.send_an_email
import com.divinelink.feature.settings.resources.star_on_github
import com.divinelink.feature.settings.resources.support_email
import org.jetbrains.compose.resources.StringResource

sealed interface SettingsModalOption {
  val icon: ImageVector
  val text: StringResource
  val url: StringResource

  data object RateGooglePlay : SettingsModalOption {
    override val icon: ImageVector = Icons.Outlined.RateReview
    override val text: StringResource = Res.string.rate_on_google_play
    override val url: StringResource = Res.string.google_play_url
  }

  data object GitHubStar : SettingsModalOption {
    override val icon: ImageVector = Icons.Outlined.Star
    override val text: StringResource = Res.string.star_on_github
    override val url: StringResource = Res.string.feature_settings_about__repository_url
  }

  data object OpenGithubIssue : SettingsModalOption {
    override val icon: ImageVector = Icons.Outlined.Warning
    override val text: StringResource = Res.string.raise_an_issue_on_github
    override val url: StringResource = Res.string.raise_github_issue_url
  }

  data object SendEmail : SettingsModalOption {
    override val icon: ImageVector = Icons.Outlined.Email
    override val text: StringResource = Res.string.send_an_email
    override val url: StringResource = Res.string.support_email
  }
}
