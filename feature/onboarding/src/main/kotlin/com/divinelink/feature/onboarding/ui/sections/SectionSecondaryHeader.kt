package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.ui.getString

@Composable
fun SectionSecondaryHeader(item: IntroSection.SecondaryHeader) {
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = MaterialTheme.dimensions.keyline_16)
      .padding(horizontal = MaterialTheme.dimensions.keyline_8),
    text = item.title.getString(),
    style = MaterialTheme.typography.titleSmall,
  )
}
