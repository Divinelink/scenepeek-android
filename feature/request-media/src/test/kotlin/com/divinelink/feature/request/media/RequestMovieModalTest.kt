package com.divinelink.feature.request.media

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstanceDetailsUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstancesUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.feature.request.media.movie.RequestMovieModal
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RequestMovieModalTest : ComposeTest() {

  private val getServerInstancesUseCase = TestGetServerInstancesUseCase()
  private val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()
  private val requestMediaUseCase = FakeRequestMediaUseCase()
  private val authRepository = TestAuthRepository()

  @Test
  fun `test show request movie dialog`() = runTest {
    getServerInstancesUseCase.mockResponse(
      Result.success(RadarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(RadarrInstanceDetailsFactory.radarr),
    )

    val viewModel = RequestMediaViewModel(
      media = MediaItemFactory.FightClub(),
      getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
      getServerInstancesUseCase = getServerInstancesUseCase.mock,
      authRepository = authRepository.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
    )

    setContentWithTheme {
      RequestMovieModal(
        viewModel = viewModel,
        media = MediaItemFactory.FightClub(),
        onDismissRequest = {},
        onNavigate = {},
        onUpdateRequestInfo = {},
        onUpdateMediaInfo = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()
      onNodeWithText(getString(UiString.core_ui_request_movie)).assertIsDisplayed()
    }
  }
}
