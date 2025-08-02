package com.divinelink.feature.onboarding.ui.sections

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
fun SectionText(item: IntroSection.Text) {
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MaterialTheme.dimensions.keyline_8),
    textAlign = TextAlign.Start,
    text = AnnotatedString.fromHtml(item.description.getString()),
    style = MaterialTheme.typography.bodyMedium,
  )
}
