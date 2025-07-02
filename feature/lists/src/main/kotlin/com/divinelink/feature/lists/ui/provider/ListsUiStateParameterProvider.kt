package com.divinelink.feature.lists.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.feature.lists.ListsUiState

class ListsUiStateParameterProvider : PreviewParameterProvider<ListsUiState> {
  override val values: Sequence<ListsUiState> = sequenceOf(
    ListsUiState(
      isLoading = true,
    ),
    ListsUiState(
      isLoading = false,
    ),
  )
}
