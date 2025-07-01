package com.divinelink.feature.lists.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.ListsUiState
import com.divinelink.feature.lists.R

class ListsUiStateParameterProvider : PreviewParameterProvider<ListsUiState> {
  override val values: Sequence<ListsUiState> = sequenceOf(
    ListsUiState(
      page = 1,
      isLoading = true,
      loadingMore = false,
      error = null,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = null,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = true,
      error = null,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = BlankSlateState.Unauthenticated(
        description = UIText.ResourceText(
          R.string.feature_lists_login_description,
        ),
      ),
    ),
  )
}
