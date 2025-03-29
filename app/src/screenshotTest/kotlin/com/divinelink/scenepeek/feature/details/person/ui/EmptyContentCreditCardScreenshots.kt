package com.divinelink.scenepeek.feature.details.person.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.provider.MediaTypeParameterProvider
import com.divinelink.feature.details.person.ui.credits.EmptyContentCreditCardPreview

@Previews
@Composable
fun EmptyContentCreditCardScreenshots(
  @PreviewParameter(MediaTypeParameterProvider::class) type: MediaType,
) {
  EmptyContentCreditCardPreview(type)
}
