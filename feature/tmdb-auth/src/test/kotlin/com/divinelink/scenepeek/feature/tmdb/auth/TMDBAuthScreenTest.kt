package com.divinelink.scenepeek.feature.tmdb.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.tmdb.auth.TMDBAuthScreen
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TMDBAuthScreenTest : ComposeTest() {

  @Test
  fun `test webViewFallback shows LoginWebViewLoginScreen`() = runTest {
    lateinit var route: Navigation
    val createSessionUseCase = TestCreateSessionUseCase()
    val createRequestTokenUseCase = FakeCreateRequestTokenUseCase()

    createRequestTokenUseCase.mockSuccess(Result.success("123456789"))

    val viewModel = TMDBAuthViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      createSessionUseCase = createSessionUseCase.mock,
    )
    setContentWithTheme {
      TMDBAuthScreen(
        onNavigate = {
          route = it
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Auth.LOGIN_WEB_VIEW_SCREEN).assertIsDisplayed()
      onNodeWithText("Login to TMDB").assertIsDisplayed()
      onNodeWithTag(TestTags.Components.TopAppBar.NAVIGATE_UP).performClick()

      route shouldBe Navigation.Back
    }
  }
}
