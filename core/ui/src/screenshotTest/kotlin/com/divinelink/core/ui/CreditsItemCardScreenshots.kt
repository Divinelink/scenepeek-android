package com.divinelink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.components.details.cast.CreditsItemCardPreview
import com.divinelink.core.ui.provider.PersonParameterProvider

@Previews
@Composable
fun CreditsSeriesItemCardScreenshot(
  @PreviewParameter(PersonParameterProvider::class) person: Person,
) {
  CreditsItemCardPreview(person)
}
