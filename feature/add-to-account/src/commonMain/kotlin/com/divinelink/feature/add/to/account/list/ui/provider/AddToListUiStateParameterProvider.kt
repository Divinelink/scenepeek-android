package com.divinelink.feature.add.to.account.list.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.media.toStub
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.resources.core_ui_login
import com.divinelink.feature.add.to.account.list.AddToListUiState
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_item_added_to_list_failure
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_item_added_to_list_success
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_list_login_description

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
      addedToLists = emptySet(),
      itemsChecked = emptySet(),
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(ListItemFactory.page1()),
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
          Res.string.feature_add_to_account_item_added_to_list_success,
          "Movies",
        ),
      ),
      error = null,
      page = 2,
      loadingMore = true,
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
          Res.string.feature_add_to_account_item_added_to_list_failure,
          "Movies",
        ),
      ),
      error = null,
      page = 2,
      loadingMore = true,
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
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
          Res.string.feature_add_to_account_list_login_description,
        ),
        retryText = UIText.ResourceText(
          UiString.core_ui_login,
        ),
      ),
      page = 2,
      loadingMore = false,
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(ListItemFactory.empty()),
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
      itemsChecked = emptySet(),
      addedToLists = emptySet(),
    ),
    AddToListUiState(
      media = MediaItemFactory.theWire().toStub(),
      isLoading = false,
      lists = ListData.Data(ListItemFactory.page1()),
      displayMessage = null,
      error = null,
      page = 1,
      loadingMore = false,
      itemsChecked = emptySet(),
      addedToLists = setOf(
        ListItemFactory.shows().id,
        ListItemFactory.recommended().id,
      ),
    ),
  )
}
