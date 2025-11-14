package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.commons.core_commons_app_name
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.feature.onboarding.Res
import com.divinelink.feature.onboarding.feature_onboarding_whats_new_title
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.commons.Res as R

@Composable
fun WhatsNewSection(whatsNew: IntroSection.WhatsNew) {
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_8),
    text = stringResource(
      Res.string.feature_onboarding_whats_new_title,
      stringResource(R.string.core_commons_app_name),
      whatsNew.version,
    ),
    style = MaterialTheme.typography.titleMedium,
  )

  HorizontalDivider()
}
