package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.ui.getString

@Composable
fun SectionHeader(item: IntroSection.Header) {
  Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_8)
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      textAlign = TextAlign.Center,
      text = item.title.getString(),
      style = MaterialTheme.typography.titleSmall,
    )

    item.description?.let { description ->
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        textAlign = TextAlign.Center,
        text = AnnotatedString.fromHtml(description.getString()),
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}
