package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ActionMenuViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = ActionMenuViewModelTestRobot()

  @Test
  fun `test mark tv show as favorite when item is non favorite`() = runTest {
    robot
      .setup(
        entryPoint = ActionMenuEntryPoint.Other(
          media = MediaItemFactory.theOffice(),
        ),
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.theOffice(),
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.theOffice(),
        result = Result.success(true),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.theOffice().copy(saved = true),
          ),
        ).copy(
          mediaState = ItemState.Data(
            item = MediaItemFactory.theOffice().copy(saved = true),
            loading = false,
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.theOffice(),
        result = Result.success(false),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.theOffice().copy(saved = false),
          ),
        ),
      )
  }

  @Test
  fun `test mark movie as favorite when item is non favorite`() = runTest {
    robot
      .setup(
        entryPoint = ActionMenuEntryPoint.Other(
          media = MediaItemFactory.FightClub(),
        ),
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.FightClub(),
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.success(true),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.FightClub().copy(saved = true),
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.success(false),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.FightClub().copy(saved = false),
          ),
        ),
      )
  }

  @Test
  fun `test mark movie as favorite with failure does nothing`() = runTest {
    robot
      .setup(
        entryPoint = ActionMenuEntryPoint.Other(
          media = MediaItemFactory.FightClub(),
        ),
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.FightClub(),
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.failure(AppException.Unknown()),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.FightClub(),
          ),
        ),
      )
  }

  @Test
  fun `test attempt to mark as favorite non media item does nothing`() = runTest {
    robot
      .setup(
        entryPoint = ActionMenuEntryPoint.Other(
          media = MediaItemFactory.Person(),
        ),
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.Person(),
          ),
        ),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = ActionMenuEntryPoint.Other(
            media = MediaItemFactory.Person(),
          ),
        ),
      )
  }

  @Test
  fun `test remove single item from list with success`() = runTest {
    val list = ActionMenuEntryPoint.ListDetails(
      media = MediaItemFactory.FightClub(),
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.success(1))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.FightClub(),
            loading = false,
          ),
          entryPoint = list,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              Res.string.feature_add_to_account_remove_single_item_success,
              "Fight Club",
              "List name",
            ),
          ),
        ),
      )
  }

  @Test
  fun `test remove single item from list with offline error`() = runTest {
    val list = ActionMenuEntryPoint.ListDetails(
      media = MediaItemFactory.FightClub(),
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Offline()))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.FightClub(),
            loading = false,
          ),
          entryPoint = list,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              Res.string.feature_add_to_account_remove_from_list_offline_error,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test remove single item from list with generic error`() = runTest {
    val list = ActionMenuEntryPoint.ListDetails(
      media = MediaItemFactory.FightClub(),
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Unknown()))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.FightClub(),
            loading = false,
          ),
          entryPoint = list,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(UiString.core_ui_error_retry),
          ),
        ),
      )
  }

  @Test
  fun `test remove single item from list with generic error and message`() = runTest {
    val list = ActionMenuEntryPoint.ListDetails(
      listId = 1234,
      listName = "List name",
      media = MediaItemFactory.FightClub(),
    )

    robot
      .setup(
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Unknown("Failed to delete.")))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.FightClub(),
            loading = false,
          ),
          entryPoint = list,
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText("Failed to delete."),
          ),
        ),
      )
      .onDismissSnackbar()
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.FightClub(),
            loading = false,
          ),
          entryPoint = list,
          snackbarMessage = null,
        ),
      )
  }

  @Test
  fun `test setup action menu with ShowProgress entry point and watching status`() = runTest {
    robot
      .mockFetchMediaItem(
        response = flowOf(
          Resource.Loading(null),
          Resource.Success(MediaItemFactory.theOffice()),
        ),
      )
      .setup(
        entryPoint = ActionMenuEntryPoint.ShowProgress(
          showId = MediaItemFactory.theOffice().id,
          status = UserShowStatus.WATCHING,
        ),
      )
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Data(
            item = MediaItemFactory.theOffice(),
            loading = false,
          ),
          entryPoint = ActionMenuEntryPoint.ShowProgress(
            showId = MediaItemFactory.theOffice().id,
            status = UserShowStatus.WATCHING,
          ),
          snackbarMessage = null,
        ),
      )
  }

  @Test
  fun `test setup action menu with ShowProgress entry point and loading`() = runTest {
    val channel = Channel<Resource<MediaItem.Media?>>()

    robot
      .mockFetchMediaItem(response = channel)
      .setup(
        entryPoint = ActionMenuEntryPoint.ShowProgress(
          showId = MediaItemFactory.theOffice().id,
          status = UserShowStatus.WATCHING,
        ),
      )
      .assertUiState(
        ActionMenuUiState(
          mediaState = ItemState.Loading,
          entryPoint = ActionMenuEntryPoint.ShowProgress(
            showId = MediaItemFactory.theOffice().id,
            status = UserShowStatus.WATCHING,
          ),
          snackbarMessage = null,
        ),
      )
      .expectUiStates(
        action = {
          launch {
            channel.send(Resource.Success(MediaItemFactory.theOffice()))
          }
        },
        uiStates = listOf(
          ActionMenuUiState(
            mediaState = ItemState.Loading,
            entryPoint = ActionMenuEntryPoint.ShowProgress(
              showId = MediaItemFactory.theOffice().id,
              status = UserShowStatus.WATCHING,
            ),
            snackbarMessage = null,
          ),
          ActionMenuUiState(
            mediaState = ItemState.Data(
              item = MediaItemFactory.theOffice(),
              loading = false,
            ),
            entryPoint = ActionMenuEntryPoint.ShowProgress(
              showId = MediaItemFactory.theOffice().id,
              status = UserShowStatus.WATCHING,
            ),
            snackbarMessage = null,
          ),
        ),
      )
  }
}
