package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.onboarding.R
import com.divinelink.core.commons.R as commonR

@Composable
fun WhatsNewSection() {
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_8),
    text = stringResource(
      R.string.feature_onboarding_whats_new_title,
      stringResource(commonR.string.core_commons_app_name),
      stringResource(commonR.string.version_name),
    ),
    style = MaterialTheme.typography.titleSmall,
  )

  HorizontalDivider()
}
