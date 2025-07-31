package com.divinelink.feature.lists.details.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.toStub
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.details.ListDetailsUiState

@ExcludeFromKoverReport
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
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
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
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
        pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
        pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
      ),
      error = BlankSlateState.Offline,
      refreshing = false,
      loadingMore = false,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
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
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
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
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
        pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
      ),
      error = BlankSlateState.Offline,
      refreshing = true,
      loadingMore = false,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch(),
        pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
      ),
      error = BlankSlateState.Offline,
      refreshing = true,
      loadingMore = true,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.mustWatch().copy(
          public = false,
          description = "",
        ),
        pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.page1(),
        pages = mapOf(),
      ),
      error = null,
      refreshing = false,
      loadingMore = true,
      selectedMediaIds = emptyList(),
      multipleSelectMode = false,
    ),
    ListDetailsUiState(
      id = 1,
      page = 1,
      details = ListDetailsData.Data(
        data = ListDetailsFactory.page1(),
        pages = mapOf(1 to ListDetailsFactory.page1().media),
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
      selectedMediaIds = MediaItemFactory.MoviesList()
        .take(2)
        .map { it.toStub() },
      multipleSelectMode = true,
    ),
  )
}
