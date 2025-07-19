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
      page = 1,
      details = ListDetailsData.Initial(
        name = ListDetailsFactory.mustWatch().name,
        backdropPath = ListDetailsFactory.mustWatch().backdropPath,
        description = ListDetailsFactory.mustWatch().description,
        public = ListDetailsFactory.mustWatch().public,
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Initial(
        name = ListDetailsFactory.mustWatch().name,
        backdropPath = "",
        description = ListDetailsFactory.mustWatch().description,
        public = ListDetailsFactory.mustWatch().public,
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
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
      page = 1,
      details = ListDetailsData.Initial(
        name = ListDetailsFactory.mustWatch().name,
        backdropPath = ListDetailsFactory.mustWatch().backdropPath,
        description = ListDetailsFactory.mustWatch().description,
        public = ListDetailsFactory.mustWatch().public,
      ),
      error = BlankSlateState.Offline,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Initial(
        name = ListDetailsFactory.mustWatch().name,
        backdropPath = "",
        description = ListDetailsFactory.mustWatch().description,
        public = false,
      ),
      error = BlankSlateState.Generic,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
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
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
      ),
      error = BlankSlateState.Offline,
      refreshing = true,
      loadingMore = true,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch().copy(
          public = false,
          description = "",
        ),
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.page1(),
      ),
      error = null,
      refreshing = false,
      loadingMore = true,
    ),
  )
}
