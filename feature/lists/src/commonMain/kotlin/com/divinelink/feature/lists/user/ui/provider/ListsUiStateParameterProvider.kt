package com.divinelink.feature.lists.user.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.ListData
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.resources.Res
import com.divinelink.feature.lists.resources.feature_lists_login_description
import com.divinelink.feature.lists.user.ListsUiState

@ExcludeFromKoverReport
class ListsUiStateParameterProvider : PreviewParameterProvider<ListsUiState> {
  override val values: Sequence<ListsUiState> = sequenceOf(
    ListsUiState(
      page = 1,
      isLoading = true,
      loadingMore = false,
      error = null,
      refreshing = false,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = null,
      refreshing = false,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = true,
      error = null,
      refreshing = false,
      lists = ListData.Data(ListItemFactory.page1()),
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = BlankSlateState.Unauthenticated(
        description = UIText.ResourceText(
          Res.string.feature_lists_login_description,
        ),
      ),
      refreshing = false,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = BlankSlateState.Offline,
      refreshing = false,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = BlankSlateState.Generic,
      refreshing = false,
      lists = ListData.Initial,
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = true,
      error = null,
      refreshing = true,
      lists = ListData.Data(ListItemFactory.page1()),
    ),
    ListsUiState(
      page = 1,
      isLoading = false,
      loadingMore = false,
      error = null,
      refreshing = false,
      lists = ListData.Data(ListItemFactory.empty()),
    ),
  )
}
