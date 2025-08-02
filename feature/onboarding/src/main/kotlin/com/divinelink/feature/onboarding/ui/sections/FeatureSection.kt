package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.getString

@Composable
fun FeatureSection(
  section: IntroSection.Feature,
  onAction: (OnboardingAction) -> Unit,
) {
  Row(
    modifier = Modifier
      .clip(MaterialTheme.shapes.large)
      .conditional(
        condition = section.isClickable,
        ifTrue = { clickable { onAction(section.action ?: return@clickable) } },
      )
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_16,
        vertical = MaterialTheme.dimensions.keyline_4,
      )
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    section.image?.let {
      Image(
        modifier = Modifier
          .size(MaterialTheme.dimensions.keyline_36),
        painter = painterResource(id = it),
        contentDescription = null,
      )
    }

    Column(
      modifier = Modifier
        .weight(0.9f)
        .padding(vertical = MaterialTheme.dimensions.keyline_8),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        text = section.title.getString(),
        style = MaterialTheme.typography.titleMedium,
      )

      Text(
        modifier = Modifier,
        textAlign = TextAlign.Start,
        text = AnnotatedString.fromHtml(section.description.getString()),
        style = MaterialTheme.typography.bodyMedium,
      )
    }

    section.action?.let { action ->
      IconButton(
        onClick = {
          if (!action.isComplete) {
            onAction(action)
          }
        },
        enabled = section.isClickable,
      ) {
        val vector = when (action) {
          is OnboardingAction.NavigateToJellyseerrLogin,
          is OnboardingAction.NavigateToTMDBLogin,
          -> if (action.isComplete) {
            Icons.Default.CheckCircleOutline
          } else {
            Icons.AutoMirrored.Filled.Login
          }
          OnboardingAction.NavigateToLinkHandling -> Icons.Default.Settings
        }

        if (action.isComplete) {
          Icons.Default.CheckCircleOutline
        } else {
          Icons.AutoMirrored.Filled.Login
        }
        Icon(
          imageVector = vector,
          contentDescription = null,
          tint = if (action.isComplete) {
            Color(0xFF4CAF50)
          } else {
            MaterialTheme.colorScheme.onSurfaceVariant
          },
        )
      }
    }
  }
}
