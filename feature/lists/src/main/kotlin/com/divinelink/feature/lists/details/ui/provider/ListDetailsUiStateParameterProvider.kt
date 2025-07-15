package com.divinelink.feature.lists.details.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.details.ListDetailsUiState

class ListDetailsUiStateParameterProvider : PreviewParameterProvider<ListDetailsUiState> {
  override val values: Sequence<ListDetailsUiState> = sequenceOf(
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Initial,
      error = null,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
      ),
      error = BlankSlateState.Offline,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Initial,
      error = BlankSlateState.Offline,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Initial,
      error = BlankSlateState.Generic,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
      ),
      error = BlankSlateState.Offline,
      refreshing = true,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      name = ListDetailsFactory.mustWatch().name,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
      ),
      error = BlankSlateState.Offline,
      refreshing = true,
      loadingMore = true,
    ),
  )
}
