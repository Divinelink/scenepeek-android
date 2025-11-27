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
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.fixtures.model.jellyseerr.request.JellyseerrMediaRequestResponseFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.jellyseerr.model.JellyseerrEditRequestMediaBodyApi
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstanceDetailsUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstancesUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_cancel
import com.divinelink.core.ui.core_ui_jellyseerr_session_expired
import com.divinelink.core.ui.core_ui_login
import com.divinelink.core.ui.core_ui_request_series
import com.divinelink.core.ui.core_ui_select_seasons_button
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.request.media.tv.RequestSeasonsModal
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class RequestSeasonsModalTest : ComposeTest() {

  private val getServerInstancesUseCase = TestGetServerInstancesUseCase()
  private val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()
  private val requestMediaUseCase = FakeRequestMediaUseCase()
  private val authRepository = TestAuthRepository()
  private val mediaRepository = TestMediaRepository()
  private val jellyseerrRepository = TestJellyseerrRepository()

  @Test
  fun `test initialise modal without advanced permission hides advanced options`() = uiTest {
    authRepository.mockPermissions(flowOf(emptyList()))

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockFailure(AppException.Unknown())
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN)
      .assertIsDisplayed()
      .performScrollToIndex(14)

    onNodeWithText(
      getString(Res.string.feature_request_media_destination_server),
    ).assertIsNotDisplayed()

    onNodeWithText(
      getString(Res.string.feature_request_media_quality_profile),
    ).assertIsNotDisplayed()

    onNodeWithText(
      getString(Res.string.feature_request_media_root_folder),
    ).assertIsNotDisplayed()
  }

  @Test
  fun `test observe profile permissions`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    authRepository.mockPermissions(flowOf(listOf(), ProfilePermission.entries))

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockFailure(AppException.Unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN)
      .performScrollToNode(
        hasText("Sonarr (Default)"),
      )

    onNodeWithText("Sonarr (Default)").assertIsDisplayed()
    onNodeWithText("Quality profile").assertIsNotDisplayed()
    onNodeWithText("Root folders").assertIsNotDisplayed()
  }

  @Test
  fun `test show request tv show dialog`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_request_series)).assertIsDisplayed()
  }

  @Test
  fun `test request tv show dialog confirm button is disabled without selected seasons`() =
    uiTest {
      mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
      jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
      getServerInstancesUseCase.mockResponse(Result.success(SonarrInstanceFactory.all))

      getServerInstanceDetailsUseCase.mockResponse(
        Result.success(SonarrInstanceDetailsFactory.sonarr),
      )

      val viewModel = RequestMediaViewModel(
        data = RequestMediaEntryData(
          request = null,
          media = MediaItemFactory.theOffice(),
        ),
        getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
        getServerInstancesUseCase = getServerInstancesUseCase.mock,
        authRepository = authRepository.mock,
        requestMediaUseCase = requestMediaUseCase.mock,
        jellyseerrRepository = jellyseerrRepository.mock,
        mediaRepository = mediaRepository.mock,
      )

      setContentWithTheme {
        RequestSeasonsModal(
          viewModel = viewModel,
          media = MediaItemFactory.theOffice(),
          onDismissRequest = {},
          onNavigate = {},
          onUpdateMediaInfo = {},
          onCancelRequest = {},
          onUpdateRequestInfo = {},
          request = null,
        )
      }

      onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()
      onNodeWithText(getString(UiString.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()
    }

  @Test
  fun `test request tv show dialog confirm button is enabled with selected seasons`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test re-selecting seasons removes them`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test request tv show dialog toggle all switch`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()

    // Toggle it back to unselect all seasons
    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()
  }

  @Test
  fun `test request tv show dialog toggle all after already have selected few seasons`() = uiTest {
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

    onNodeWithText("Season 1").performClick()
    onNodeWithText("Season 2").performClick()
    onNodeWithText("Season 3").performClick()

    onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()
  }

  @Test
  fun `test request tv show modal already processed seasons are not clickable`() = uiTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()

    onNodeWithText("Season 1").performClick()
    onNodeWithText("Season 2").performClick()
    onNodeWithText("Season 3").performClick()

    onNodeWithText(getString(UiString.core_ui_select_seasons_button)).assertIsDisplayed()
  }

  @Test
  fun `test request with forbidden 403 prompts to re-login`() = uiTest {
    var route: Navigation? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Forbidden())
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {
          route = it
        },
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons")
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithTag(TestTags.Dialogs.TWO_BUTTON_DIALOG).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_jellyseerr_session_expired)).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_login)).assertIsDisplayed().performClick()

    route shouldBe Navigation.JellyseerrSettingsRoute(withNavigationBar = true)
  }

  @Test
  fun `test request with unauthorized 401 prompts to re-login`() = uiTest {
    var route: Navigation? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Unauthorized())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {
          route = it
        },
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons")
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithTag(TestTags.Dialogs.TWO_BUTTON_DIALOG).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_jellyseerr_session_expired)).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_login)).assertIsDisplayed().performClick()

    route shouldBe Navigation.JellyseerrSettingsRoute(withNavigationBar = true)
  }

  @Test
  fun `test request with conflict 409 shows snackbar`() = uiTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    requestMediaUseCase.mockFailure(AppException.Conflict())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons")
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
      text = UIText.ResourceText(Res.string.feature_request_media_request_exists),
    )
  }

  @Test
  fun `test request with generic error shows generic message`() = uiTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    requestMediaUseCase.mockFailure(AppException.Unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
    onNodeWithText("Request 9 seasons")
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
      text = UIText.ResourceText(
        Res.string.feature_request_media_failed_request,
        "The Office",
      ),
    )
  }

  @Test
  fun `test fetch details with empty instances does not show advanced options`() = uiTest {
    getServerInstancesUseCase.mockResponse(Result.success(emptyList()))
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN)
      .assertIsDisplayed()
      .performScrollToIndex(14)

    onNodeWithText(
      getString(Res.string.feature_request_media_quality_profile),
    ).assertIsNotDisplayed()
    onNodeWithText(
      getString(Res.string.feature_request_media_root_folder),
    ).assertIsNotDisplayed()
  }

  @Test
  fun `test fetch details with failure`() = uiTest {
    getServerInstancesUseCase.mockFailure(AppException.Unknown())
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN)
      .assertIsDisplayed()
      .performScrollToIndex(14)

    onNodeWithText(
      getString(Res.string.feature_request_media_quality_profile),
    ).assertIsNotDisplayed()
    onNodeWithText(
      getString(Res.string.feature_request_media_root_folder),
    ).assertIsNotDisplayed()
  }

  @Test
  fun `test update destination server`() = uiTest {
    authRepository.mockEnableAllPermissions()
    mediaRepository.mockFetchTvSeasons(response = Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test update quality profile`() = uiTest {
    authRepository.mockEnableAllPermissions()
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test update root folder`() = uiTest {
    authRepository.mockEnableAllPermissions()
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )
    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test request season with updated properties`() = uiTest {
    var mediaInfo: JellyseerrMediaInfo? = null
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    authRepository.mockEnableAllPermissions()
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
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {
          mediaInfo = it
        },
        request = null,
        onCancelRequest = {},
        onUpdateRequestInfo = {},
      )
    }

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
    onNodeWithText("data/tv (1.78 TB) (Default)").assertIsNotDisplayed()

    onNodeWithText("Request 9 seasons")
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    viewModel.uiState.value.snackbarMessage shouldBe SnackbarMessage.from(
      text = UIText.ResourceText(
        Res.string.feature_request_media_success_request,
        "The Office",
      ),
    )

    mediaInfo shouldBe JellyseerrMediaRequestResponseFactory.tvPartially().mediaInfo
  }

  @Test
  fun `test fetch instance details with error hides fields`() = uiTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    authRepository.mockEnableAllPermissions()
    getServerInstanceDetailsUseCase.mockFailure(AppException.Unknown())
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )
    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

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

  @Test
  fun `test initialise modal with request sets preselected advanced values`() = uiTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )
    authRepository.mockEnableAllPermissions()
    getServerInstanceDetailsUseCase.mockResponse(
      response = Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.all()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = JellyseerrRequestFactory.Tv.theOffice1(),
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )
    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText("Animarr"),
    )

    onNodeWithText("Animarr").assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText("Quality profile"),
    )

    onNodeWithText("Quality profile").assertIsDisplayed()
    onNodeWithText("Any").assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText("/data/anime (1.78 TB)"),
    )

    onNodeWithText("Root folder").assertIsDisplayed()
    onNodeWithText("/data/anime (1.78 TB)").assertIsDisplayed()
  }

  @Test
  fun `test with request and selected seasons edit request button is shown`() = uiTest {
    var cancelRequestId: Int? = null

    authRepository.mockEnableAllPermissions()
    getServerInstancesUseCase.mockResponse(
      response = Result.success(SonarrInstanceFactory.all),
    )
    getServerInstanceDetailsUseCase.mockResponse(
      response = Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = JellyseerrRequestFactory.Tv.theOffice1(),
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )
    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = { requestId ->
          cancelRequestId = requestId
        },
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithText("Edit request").assertIsDisplayed()

    // Pre-selected season from request is enabled
    // Toggle off first requested season
    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(1))
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithText("Edit request").assertIsDisplayed()

    // Requested season that is not on the request is not enabled
    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(2))
      .assertIsDisplayed()
      .assertIsNotEnabled()

    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(3))
      .assertIsDisplayed()
      .assertIsNotEnabled()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasTestTag(TestTags.Dialogs.SEASON_SWITCH.format(6)),
    )

    // Toggle off second requested season
    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(6))
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    // All requested season are toggled off - cancel request is shown
    onNodeWithText("Edit request").assertIsNotDisplayed()
    onNodeWithText("Cancel request").assertIsDisplayed().performClick()

    cancelRequestId shouldBe JellyseerrRequestFactory.Tv.theOffice1().id
  }

  @Test
  fun `test cancel request with failure`() = uiTest {
    var cancelRequestId: Int? = null

    authRepository.mockEnableAllPermissions()
    getServerInstancesUseCase.mockResponse(
      response = Result.success(SonarrInstanceFactory.all),
    )
    getServerInstanceDetailsUseCase.mockResponse(
      response = Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    jellyseerrRepository.mockDeleteRequest(Result.failure(AppException.Unknown()))

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = JellyseerrRequestFactory.Tv.theOffice1(),
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )
    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = { requestId ->
          cancelRequestId = requestId
        },
        onUpdateRequestInfo = {},
        request = null,
      )
    }

    onNodeWithText("Edit request").assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(1))
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasTestTag(TestTags.Dialogs.SEASON_SWITCH.format(6)),
    )

    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(6))
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithText("Cancel request").assertIsDisplayed().performClick()

    cancelRequestId shouldBe null
  }

  @Test
  fun `test edit request`() = uiTest {
    var jellyseerrRequest: JellyseerrRequest? = null

    authRepository.mockEnableAllPermissions()
    getServerInstancesUseCase.mockResponse(
      response = Result.success(SonarrInstanceFactory.all),
    )
    getServerInstanceDetailsUseCase.mockResponse(
      response = Result.success(SonarrInstanceDetailsFactory.sonarr),
    )
    mediaRepository.mockFetchTvSeasons(Result.success(SeasonFactory.allWithStatus()))
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.unknown())
    jellyseerrRepository.mockEditRequest(
      request = JellyseerrEditRequestMediaBodyApi(
        requestId = 5,
        mediaType = MediaType.TV.value,
        mediaId = 2316,
        is4k = false,
        seasons = listOf(6),
        serverId = 1,
        profileId = 3,
        rootFolder = "/data/tv",
      ),
      response = Result.success(
        JellyseerrRequestFactory.Tv.theOffice1().copy(
          profileName = InstanceProfileFactory.hd720.name,
          profileId = InstanceProfileFactory.hd720.id,
          rootFolder = InstanceRootFolderFactory.tv.path,
        ),
      ),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = JellyseerrRequestFactory.Tv.theOffice1(),
        media = MediaItemFactory.theOffice(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )
    setContentWithTheme {
      RequestSeasonsModal(
        viewModel = viewModel,
        media = MediaItemFactory.theOffice(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
        onUpdateRequestInfo = { updateInfo ->
          jellyseerrRequest = updateInfo
        },
        request = null,
      )
    }

    onNodeWithText("Edit request").assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(1))
      .assertIsDisplayed()
      .assertIsEnabled()
      .performClick()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText("Quality profile"),
    )

    onNodeWithText("Quality profile").performClick()
    onNodeWithText(InstanceProfileFactory.hd720.name).performClick()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText("Root folder"),
    )

    // Preselected folder path
    onNodeWithText("/data/anime (1.78 TB)").assertIsDisplayed()
    onNodeWithText("Root folder").performClick()

    onNodeWithText("/data/tv (1.78 TB) (Default)").performClick()

    onNodeWithText("Edit request").assertIsDisplayed().performClick()
    jellyseerrRequest shouldBe JellyseerrRequestFactory.Tv.theOffice1().copy(
      profileName = InstanceProfileFactory.hd720.name,
      profileId = InstanceProfileFactory.hd720.id,
      rootFolder = InstanceRootFolderFactory.tv.path,
    )
  }
}
