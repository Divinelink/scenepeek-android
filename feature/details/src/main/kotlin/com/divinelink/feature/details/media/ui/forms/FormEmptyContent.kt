package com.divinelink.feature.details.media.ui.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState

@Composable
fun FormEmptyContent(
  modifier: Modifier,
  title: UIText,
  description: UIText,
) {
  Column(modifier = modifier) {
    BlankSlate(
      modifier = Modifier.weight(1f),
      uiState = BlankSlateState.Custom(
        title = title,
        description = description,
      ),
    )

    Spacer(modifier = Modifier.weight(2f))
  }
}
