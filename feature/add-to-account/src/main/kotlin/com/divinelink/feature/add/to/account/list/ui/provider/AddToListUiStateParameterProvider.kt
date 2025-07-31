package com.divinelink.feature.add.to.account.list.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.media.toStub
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.AddToListUiState

@ExcludeFromKoverReport
class AddToListUiStateParameterProvider : PreviewParameterProvider<AddToListUiState> {
  override val values: Sequence<AddToListUiState> = sequenceOf(
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Initial,
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(ListItemFactory.page1()),
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = null,
      error = null,
      page = 2,
      loadingMore = false,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = true,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = null,
      error = null,
      page = 2,
      loadingMore = false,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = null,
      error = null,
      page = 2,
      loadingMore = true,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = DisplayMessage.Success(
        UIText.ResourceText(
          R.string.feature_add_to_account_item_added_to_list_success,
          "Movies",
        ),
      ),
      error = null,
      page = 2,
      loadingMore = true,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = DisplayMessage.Error(
        UIText.ResourceText(
          R.string.feature_add_to_account_item_added_to_list_failure,
          "Movies",
        ),
      ),
      error = null,
      page = 2,
      loadingMore = true,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(
        ListItemFactory.page2().copy(
          list = ListItemFactory.page1().list + ListItemFactory.page2().list,
        ),
      ),
      displayMessage = null,
      error = BlankSlateState.Unauthenticated(
        description = UIText.ResourceText(
          R.string.feature_add_to_account_list_login_description,
        ),
        retryText = UIText.ResourceText(
          com.divinelink.core.ui.R.string.core_ui_login,
        ),
      ),
      page = 2,
      loadingMore = false,
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(ListItemFactory.empty()),
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
    ),
  )
}
