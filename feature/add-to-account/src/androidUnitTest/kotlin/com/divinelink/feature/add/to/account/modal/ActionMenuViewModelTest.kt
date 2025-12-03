package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
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
        mediaItem = MediaItemFactory.theOffice(),
        entryPoint = ActionMenuEntryPoint.Other,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.theOffice(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.theOffice(),
        result = Result.success(true),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.theOffice().copy(isFavorite = true),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.theOffice(),
        result = Result.success(false),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.theOffice().copy(isFavorite = false),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
  }

  @Test
  fun `test mark movie as favorite when item is non favorite`() = runTest {
    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = ActionMenuEntryPoint.Other,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.success(true),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub().copy(isFavorite = true),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.success(false),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub().copy(isFavorite = false),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
  }

  @Test
  fun `test mark movie as favorite with failure does nothing`() = runTest {
    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = ActionMenuEntryPoint.Other,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = MediaItemFactory.FightClub(),
        result = Result.failure(AppException.Unknown()),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
  }

  @Test
  fun `test attempt to mark as favorite non media item does nothing`() = runTest {
    robot
      .setup(
        mediaItem = MediaItemFactory.Person(),
        entryPoint = ActionMenuEntryPoint.Other,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.Person(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
      .onMarkAsFavorite()
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.Person(),
          entryPoint = ActionMenuEntryPoint.Other,
        ),
      )
  }

  @Test
  fun `test remove single item from list with success`() = runTest {
    val list = ActionMenuEntryPoint.ListDetails(
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.success(1))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          media = MediaItemFactory.FightClub(),
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
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Offline()))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          media = MediaItemFactory.FightClub(),
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
      listId = 1234,
      listName = "List name",
    )

    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Unknown()))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          media = MediaItemFactory.FightClub(),
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
    )

    robot
      .setup(
        mediaItem = MediaItemFactory.FightClub(),
        entryPoint = list,
      )
      .assertUiState(
        ActionMenuUiState.initial(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
        ),
      )
      .mockRemoveFromList(Result.failure(AppException.Unknown("Failed to delete.")))
      .onRemoveItemsFromList()
      .assertUiState(
        ActionMenuUiState(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText("Failed to delete."),
          ),
        ),
      )
      .onDismissSnackbar()
      .assertUiState(
        ActionMenuUiState(
          media = MediaItemFactory.FightClub(),
          entryPoint = list,
          snackbarMessage = null,
        ),
      )
  }
}
