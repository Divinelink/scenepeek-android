package com.divinelink.feature.settings.app.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestMediaRatingPreferenceUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_movie_rating_preference
import com.divinelink.feature.settings.resources.feature_settings_tv_rating_preference
import org.jetbrains.compose.resources.getString
import kotlin.test.BeforeTest
import kotlin.test.Test

class DetailsPreferencesSettingsScreenTest : ComposeTest() {

  private lateinit var mediaRatingPreferenceUseCase: TestMediaRatingPreferenceUseCase
  private val preferencesRepository = TestPreferencesRepository()

  @BeforeTest
  override fun setUp() {
    mediaRatingPreferenceUseCase = TestMediaRatingPreferenceUseCase()
  }

  @Test
  fun `test select and update movie rating preference`() = uiTest {
    val viewModel = DetailsPreferencesViewModel(
      preferencesRepository = preferencesRepository,
      mediaRatingPreferenceUseCase = mediaRatingPreferenceUseCase.mock(
        movieRatingSource = RatingSource.IMDB,
        tvRatingSource = RatingSource.TMDB,
      ),
    )

    setVisibilityScopeContent {
      DetailPreferencesSettingsScreen(
        viewModel = viewModel,
        onNavigateUp = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithText(RatingSource.IMDB.value).assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToNode(
      hasText(getString(Res.string.feature_settings_tv_rating_preference)),
    )

    onNodeWithText(
      getString(Res.string.feature_settings_movie_rating_preference),
    ).assertIsDisplayed()
      .performClick()

    onNodeWithTag(
      TestTags.Settings.RADIO_BUTTON_SELECT_OPTION.format(RatingSource.TRAKT.value),
    ).assertIsDisplayed().performClick()

    onNodeWithText(RatingSource.IMDB.value).assertIsNotDisplayed()
    onNodeWithText(RatingSource.TRAKT.value).assertIsDisplayed()
  }

  @Test
  fun `test select and update tv rating preference`() = uiTest {
    val viewModel = DetailsPreferencesViewModel(
      preferencesRepository = preferencesRepository,
      mediaRatingPreferenceUseCase = mediaRatingPreferenceUseCase.mock(
        movieRatingSource = RatingSource.TMDB,
        tvRatingSource = RatingSource.IMDB,
      ),
    )

    setVisibilityScopeContent {
      DetailPreferencesSettingsScreen(
        viewModel = viewModel,
        onNavigateUp = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToIndex(5)

    onNodeWithText(RatingSource.IMDB.value).assertIsDisplayed()

    onNodeWithText(getString(Res.string.feature_settings_tv_rating_preference)).assertIsDisplayed()
      .performClick()

    onNodeWithTag(
      TestTags.Settings.RADIO_BUTTON_SELECT_OPTION.format(RatingSource.TRAKT.value),
    ).assertIsDisplayed()
      .performClick()

    onNodeWithText(RatingSource.IMDB.value).assertIsNotDisplayed()
    onNodeWithText(RatingSource.TRAKT.value).assertIsDisplayed()
  }
}
