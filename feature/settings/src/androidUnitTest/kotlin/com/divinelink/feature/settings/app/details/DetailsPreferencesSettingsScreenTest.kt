package com.divinelink.feature.settings.app.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestMediaRatingPreferenceUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_movie_rating_preference
import com.divinelink.feature.settings.feature_settings_tv_rating_preference
import org.jetbrains.compose.resources.getString
import kotlin.test.BeforeTest
import kotlin.test.Test

class DetailsPreferencesSettingsScreenTest : ComposeTest() {

  private lateinit var mediaRatingPreferenceUseCase: TestMediaRatingPreferenceUseCase

  @BeforeTest
  override fun setUp() {
    mediaRatingPreferenceUseCase = TestMediaRatingPreferenceUseCase()
  }

  @Test
  fun `test select and update movie rating preference`() = uiTest {
    val viewModel = DetailsPreferencesViewModel(
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
