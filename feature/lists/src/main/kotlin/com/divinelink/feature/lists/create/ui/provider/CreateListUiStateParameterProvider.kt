package com.divinelink.feature.lists.create.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.feature.lists.create.CreateListUiState

class CreateListUiStateParameterProvider : PreviewParameterProvider<CreateListUiState> {
  override val values: Sequence<CreateListUiState> = sequenceOf(
    CreateListUiState.initial,
  )
}
