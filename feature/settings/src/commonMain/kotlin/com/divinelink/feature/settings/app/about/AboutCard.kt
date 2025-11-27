package com.divinelink.feature.settings.app.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.core_model_ic_tmdb
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_about__ad_free
import com.divinelink.feature.settings.feature_settings_about__developed_by
import com.divinelink.feature.settings.feature_settings_about__developer_github_url
import com.divinelink.feature.settings.feature_settings_about__github
import com.divinelink.feature.settings.feature_settings_about__no_tracker
import com.divinelink.feature.settings.feature_settings_about__open_source
import com.divinelink.feature.settings.feature_settings_about__repository_url
import com.divinelink.feature.settings.feature_settings_about__source_code
import com.divinelink.feature.settings.feature_settings_about__tmdb_endorsement
import com.divinelink.feature.settings.feature_settings_about__version
import com.divinelink.feature.settings.ic_ad_free
import com.divinelink.feature.settings.ic_github
import com.divinelink.feature.settings.ic_open_source
import com.divinelink.feature.settings.ic_tracker_free
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.model.Res as R

@Composable
fun AboutCard(
  modifier: Modifier = Modifier,
  name: String,
  version: String,
  github: String,
  onNavigate: (Navigation) -> Unit,
) {
  val urlHandler = rememberUrlHandler()
  val repositoryUrl = stringResource(Res.string.feature_settings_about__repository_url)
  val githubAccountUrl = stringResource(
    Res.string.feature_settings_about__developer_github_url,
    github,
  )
  val aboutGithubTitle = stringResource(Res.string.feature_settings_about__github)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag(TestTags.Settings.About.CARD)
      .padding(MaterialTheme.dimensions.keyline_16),
    elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimensions.keyline_4),
  ) {
    Column(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_24)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      // App Logo
      Surface(
        modifier = Modifier
          .size(MaterialTheme.dimensions.keyline_96)
          .clip(CircleShape),
        color = MaterialTheme.colorScheme.surfaceVariant,
      ) {
        Image(
          painter = painterResource(R.drawable.core_model_ic_tmdb),
          contentDescription = "ScenePeek Logo",
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          contentScale = ContentScale.Fit,
        )
      }

      // App Name and Version
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          text = name,
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
        )
        Text(
          text = stringResource(Res.string.feature_settings_about__version, version),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      ScenePeekFeatures()

      // TMDB Attribution
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        Text(
          text = stringResource(Res.string.feature_settings_about__tmdb_endorsement),
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        FlowRow(
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          itemVerticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = stringResource(Res.string.feature_settings_about__developed_by, github),
            style = MaterialTheme.typography.bodyMedium,
          )

          TextButton(
            onClick = {
              urlHandler.openUrl(
                url = githubAccountUrl,
                onError = {
                  onNavigate(
                    Navigation.WebViewRoute(
                      url = githubAccountUrl,
                      title = aboutGithubTitle,
                    ),
                  )
                },
              )
            },
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              Icon(
                painter = painterResource(Res.drawable.ic_github),
                contentDescription = "GitHub Account",
                modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
              )
              Text(github)
            }
          }
        }

        TextButton(
          onClick = {
            urlHandler.openUrl(
              url = repositoryUrl,
              onError = {
                onNavigate(
                  Navigation.WebViewRoute(
                    url = repositoryUrl,
                    title = aboutGithubTitle,
                  ),
                )
              },
            )
          },
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = "Source Code",
              modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
            )
            Text(
              text = stringResource(Res.string.feature_settings_about__source_code),
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ScenePeekFeatures() {
  Card(
    modifier = Modifier.wrapContentSize(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
  ) {
    FlowRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.dimensions.keyline_16),
      itemVerticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      FeatureItem(
        icon = painterResource(Res.drawable.ic_open_source),
        text = stringResource(Res.string.feature_settings_about__open_source),
      )
      FeatureItem(
        icon = painterResource(Res.drawable.ic_ad_free),
        text = stringResource(Res.string.feature_settings_about__ad_free),
      )
      FeatureItem(
        icon = painterResource(Res.drawable.ic_tracker_free),
        text = stringResource(Res.string.feature_settings_about__no_tracker),
      )
    }
  }
}

@Composable
private fun FeatureItem(
  icon: Painter,
  text: String,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Icon(
      painter = icon,
      contentDescription = text,
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_24),
      tint = MaterialTheme.colorScheme.primary,
    )
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
    )
  }
}

@Previews
@Composable
fun AboutCardPreview() {
  AppTheme {
    Surface {
      AboutCard(
        modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
        name = "ScenePeek",
        version = "1.0.0",
        github = "Divinelink",
        onNavigate = {},
      )
    }
  }
}
