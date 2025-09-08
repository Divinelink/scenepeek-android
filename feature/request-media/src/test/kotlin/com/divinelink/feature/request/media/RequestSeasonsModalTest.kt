package com.divinelink.feature.request.media

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.jellyseerr.request.JellyseerrMediaRequestResponseFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstanceDetailsUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstancesUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.request.media.tv.RequestSeasonsModal
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RequestSeasonsModalTest : ComposeTest() {

  private val getServerInstancesUseCase = TestGetServerInstancesUseCase()
  private val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()
  private val requestMediaUseCase = FakeRequestMediaUseCase()

  @Test
  fun `test show request tv show dialog`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        seasons = SeasonFactory.allWithStatus(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_request_series)).assertIsDisplayed()
    }
  }

  @Test
  fun `test request tv show dialog confirm button is disabled without selected seasons`() =
    runTest {
      getServerInstancesUseCase.mockResponse(Result.success(SonarrInstanceFactory.all))

      getServerInstanceDetailsUseCase.mockResponse(
        Result.success(SonarrInstanceDetailsFactory.sonarr),
      )

      val viewModel = RequestMediaViewModel(
        media = MediaItemFactory.theOffice(),
        getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
        getServerInstancesUseCase = getServerInstancesUseCase.mock,
        requestMediaUseCase = requestMediaUseCase.mock,
      )

      setContentWithTheme {
        RequestSeasonsModal(
          viewModel = viewModel,
          media = MediaItemFactory.theOffice(),
          seasons = SeasonFactory.allWithStatus(),
          onDismissRequest = {},
          onNavigate = {},
          onUpdateMediaInfo = {},
        )
      }

      with(composeTestRule) {
        onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
        onNodeWithText(getString(UiString.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()
        onNodeWithText(getString(UiString.core_ui_select_seasons_button))
          .assertIsDisplayed()
          .assertIsNotEnabled()
      }
    }

  @Test
  fun `test request tv show dialog confirm button is enabled with selected seasons`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        seasons = SeasonFactory.all(),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()

      onNodeWithTag(TestTags.Dialogs.SEASON_ROW.format(1)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_ROW.format(2)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(3)).performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText("Season 1").performClick()

      onNodeWithText("Request 2 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText("Season 2").performClick()

      onNodeWithText("Request 1 season").assertIsDisplayed().assertIsEnabled()
    }
  }

  @Test
  fun `test re-selecting seasons removes them`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        seasons = SeasonFactory.all(),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()

      onNodeWithText(getString(UiString.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()

      onNodeWithText("Request 3 seasons").assertDoesNotExist()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertDoesNotExist()

      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(1)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(2)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(3)).performClick()

      onNodeWithText(getString(UiString.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()
    }
  }

  @Test
  fun `test request tv show dialog toggle all switch`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        seasons = SeasonFactory.all(),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()

      // Toggle it back to unselect all seasons
      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()
    }
  }

  @Test
  fun `test request tv show dialog toggle all after already have selected few seasons`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        seasons = SeasonFactory.all(),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()
    }
  }

  @Test
  fun `test request tv show modal already processed seasons are not clickable`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.allWithStatus(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()
    }
  }

  @Test
  fun `test request with forbidden 403 prompts to re-login`() = runTest {
    var route: Navigation? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Forbidden())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {
          route = it
        },
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons")
        .assertIsDisplayed()
        .assertIsEnabled()
        .performClick()

      onNodeWithTag(TestTags.Dialogs.TWO_BUTTON_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_jellyseerr_session_expired)).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_login)).assertIsDisplayed().performClick()
    }
    route shouldBe Navigation.JellyseerrSettingsRoute(withNavigationBar = true)
  }

  @Test
  fun `test request with unauthorized 401 prompts to re-login`() = runTest {
    var route: Navigation? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Unauthorized())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {
          route = it
        },
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons")
        .assertIsDisplayed()
        .assertIsEnabled()
        .performClick()

      onNodeWithTag(TestTags.Dialogs.TWO_BUTTON_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_jellyseerr_session_expired)).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_login)).assertIsDisplayed().performClick()
    }

    route shouldBe Navigation.JellyseerrSettingsRoute(withNavigationBar = true)
  }

  @Test
  fun `test request with conflict 409 shows snackbar`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Conflict())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons")
        .assertIsDisplayed()
        .assertIsEnabled()
        .performClick()

      viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
        text = UIText.ResourceText(R.string.feature_request_media_request_exists),
      )
    }
  }

  @Test
  fun `test request with generic error shows generic message`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Unknown())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons")
        .assertIsDisplayed()
        .assertIsEnabled()
        .performClick()

      viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
        text = UIText.ResourceText(
          R.string.feature_request_media_failed_request,
          "The Office",
        ),
      )
    }
  }

  @Test
  fun `test fetch details with empty instances does not show advanced options`() = runTest {
    getServerInstancesUseCase.mockResponse(Result.success(emptyList()))

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .assertIsDisplayed()
        .performScrollToIndex(14)

      onNodeWithText(
        getString(R.string.feature_request_media_quality_profile),
      ).assertIsNotDisplayed()
      onNodeWithText(
        getString(R.string.feature_request_media_root_folder),
      ).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test fetch details with failure`() = runTest {
    getServerInstancesUseCase.mockFailure(AppException.Unknown())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .assertIsDisplayed()
        .performScrollToIndex(14)

      onNodeWithText(
        getString(R.string.feature_request_media_quality_profile),
      ).assertIsNotDisplayed()
      onNodeWithText(
        getString(R.string.feature_request_media_root_folder),
      ).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test update destination server`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .performScrollToNode(
          hasTestTag(TestTags.Request.DESTINATION_SERVER_MENU.format("Content")),
        )

      onNodeWithText("Sonarr (Default)").assertIsDisplayed()
      onNodeWithText("Animarr").assertIsNotDisplayed()

      onNodeWithTag(TestTags.Request.DESTINATION_SERVER_MENU.format("Content"))
        .assertIsDisplayed()
        .performClick()

      onNodeWithText("Animarr").assertIsDisplayed().performClick()
      onNodeWithText("Sonarr (Default)").assertIsNotDisplayed()
    }
  }

  @Test
  fun `test update quality profile`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .performScrollToNode(hasTestTag(TestTags.Request.QUALITY_PROFILE_MENU.format("Content")))

      onNodeWithText("HD - 720p/1080p (Default)").assertIsDisplayed()
      onNodeWithText("SD").assertIsNotDisplayed()

      onNodeWithTag(TestTags.Request.QUALITY_PROFILE_MENU.format("Content"))
        .assertIsDisplayed()
        .performClick()

      onNodeWithText("SD").assertIsDisplayed().performClick()
      onNodeWithText("HD - 720p/1080p (Default)").assertIsNotDisplayed()
    }
  }

  @Test
  fun `test update root folder`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .performScrollToNode(hasTestTag(TestTags.Request.ROOT_FOLDER_MENU.format("Content")))

      onNodeWithText("/data/tv (1.78 TB) (Default)").assertIsDisplayed()
      onNodeWithText("/data/anime (1.78 TB)").assertIsNotDisplayed()

      onNodeWithTag(TestTags.Request.ROOT_FOLDER_MENU.format("Content"))
        .assertIsDisplayed()
        .performClick()

      onNodeWithText("/data/anime (1.78 TB)").assertIsDisplayed().performClick()
      onNodeWithText("HD data/tv (1.78 TB) (Default)").assertIsNotDisplayed()
    }
  }

  @Test
  fun `test request season with updated properties`() = runTest {
    var mediaInfo: JellyseerrMediaInfo? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockSuccess(
      JellyseerrRequestParams(
        mediaId = MediaItemFactory.theOffice().id,
        mediaType = MediaType.TV.value,
        is4k = false,
        seasons = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
        serverId = 0,
        profileId = 6,
        rootFolder = "/data/anime",
      ),
      response = Result.success(JellyseerrMediaRequestResponseFactory.tvPartially()),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {
          mediaInfo = it
        },
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .performScrollToNode(hasTestTag(TestTags.Request.ROOT_FOLDER_MENU.format("Content")))

      onNodeWithText("/data/tv (1.78 TB) (Default)").assertIsDisplayed()
      onNodeWithText("/data/anime (1.78 TB)").assertIsNotDisplayed()

      onNodeWithTag(TestTags.Request.ROOT_FOLDER_MENU.format("Content"))
        .assertIsDisplayed()
        .performClick()

      onNodeWithText("/data/anime (1.78 TB)").assertIsDisplayed().performClick()
      onNodeWithText("HD data/tv (1.78 TB) (Default)").assertIsNotDisplayed()

      onNodeWithText("Request 9 seasons")
        .assertIsDisplayed()
        .assertIsEnabled()
        .performClick()

      viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
        text = UIText.ResourceText(
          R.string.feature_request_media_success_request,
          "The Office",
        ),
      )

      mediaInfo shouldBe JellyseerrMediaRequestResponseFactory.tvPartially().mediaInfo
    }
  }

  @Test
  fun `test fetch instance details with error hides fields`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockFailure(AppException.Unknown())

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.theOffice(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        seasons = SeasonFactory.all(),
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN)
        .performScrollToNode(
          hasText("Sonarr (Default)"),
        )

      onNodeWithText("Sonarr (Default)").assertIsDisplayed()

      onNodeWithTag(TestTags.LAZY_COLUMN).performTouchInput { swipeDown() }

      onNodeWithText("Quality profile").assertIsNotDisplayed()
      onNodeWithText("Root folders").assertIsNotDisplayed()
    }
  }
}
