package com.divinelink.feature.request.media

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstanceDetailsUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstancesUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.feature.request.media.movie.RequestMovieModal
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RequestMovieModalTest : ComposeTest() {

  private val getServerInstancesUseCase = TestGetServerInstancesUseCase()
  private val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()
  private val requestMediaUseCase = FakeRequestMediaUseCase()
  private val authRepository = TestAuthRepository()
  private val jellyseerrRepository = TestJellyseerrRepository()
  private val mediaRepository = TestMediaRepository()

  @Test
  fun `test show request movie dialog`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(RadarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(RadarrInstanceDetailsFactory.radarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = null,
        media = MediaItemFactory.FightClub(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      authRepository = authRepository.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestMovieModal(
        request = null,
        media = MediaItemFactory.FightClub(),
        viewModel = viewModel,
        onDismissRequest = {},
        onNavigate = {},
        onUpdateRequestInfo = {},
        onUpdateMediaInfo = {},
        onCancelRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_request_movie)).assertIsDisplayed()
    }
  }

  @Test
  fun `test cancel request on request movie modal`() = runTest {
    var cancelRequestId: Int? = null

    getServerInstancesUseCase.mockResponse(
      Result.success(RadarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(RadarrInstanceDetailsFactory.radarr),
    )

    val viewModel = RequestMediaViewModel(
      data = RequestMediaEntryData(
        request = JellyseerrRequestFactory.movie(),
        media = MediaItemFactory.FightClub(),
      ),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      authRepository = authRepository.mock,
      jellyseerrRepository = jellyseerrRepository.mock,
      mediaRepository = mediaRepository.mock,
    )

    setContentWithTheme {
      RequestMovieModal(
        request = JellyseerrRequestFactory.movie(),
        media = MediaItemFactory.FightClub(),
        viewModel = viewModel,
        onDismissRequest = {},
        onNavigate = {},
        onUpdateRequestInfo = {},
        onUpdateMediaInfo = {},
        onCancelRequest = { cancelRequestId = it },
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_edit_request)).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_cancel_request)).assertIsDisplayed().performClick()

      cancelRequestId shouldBe 1

      jellyseerrRepository.verifyTvDetailsInteractions(0)
    }
  }
}
