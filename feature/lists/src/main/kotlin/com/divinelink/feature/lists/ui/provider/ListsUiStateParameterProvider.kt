package com.divinelink.feature.lists.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.ListData
import com.divinelink.feature.lists.ListsUiState
import com.divinelink.feature.lists.R

class ListsUiStateParameterProvider : PreviewParameterProvider<ListsUiState> {
  override val values: Sequence<ListsUiState> = sequenceOf(
    ListsUiState(
      page = 1,
      isLoading = true,
      loadingMore = false,
      error = null,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = null,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = true,
      error = null,
      lists = ListData.Data(ListItemFactory.paginationData()),
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
      lists = ListData.Initial,
    ),
  )
}
