package com.divinelink.feature.settings.app.details

import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import org.junit.Rule
import kotlin.test.Test

class DetailsPreferencesViewModelTest {

  private val robot = DetailsPreferencesViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test initial values on init`() {
    robot
      .buildViewModel()
      .assertUiState(DetailsPreferencesUiState.initial())
  }

  @Test
  fun `test init with difference from initial values for tv and movie`() {
    robot
      .mockTvRatingSource(RatingSource.IMDB)
      .mockMovieRatingSource(RatingSource.TRAKT)
      .buildViewModel()
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TRAKT,
          tvSource = RatingSource.IMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `test set movie rating source`() {
    robot
      .buildViewModel()
      .onSetMovieMediaRatingSource(RatingSource.IMDB)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.IMDB,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
      .onSetMovieMediaRatingSource(RatingSource.TRAKT)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TRAKT,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `test set tv rating source`() {
    robot
      .buildViewModel()
      .onSetTvMediaRatingSource(RatingSource.IMDB)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.IMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
      .onSetTvMediaRatingSource(RatingSource.TRAKT)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.TRAKT,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `test set episodes rating source`() {
    robot
      .buildViewModel()
      .onSetEpisodesMediaRatingSource(RatingSource.IMDB)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.IMDB,
          seasonsSource = RatingSource.TMDB,
        ),
      )
      .onSetEpisodesMediaRatingSource(RatingSource.TRAKT)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.TRAKT,
          seasonsSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `test set seasons rating source`() {
    robot
      .buildViewModel()
      .onSetSeasonsMediaRatingSource(RatingSource.IMDB)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.IMDB,
        ),
      )
      .onSetSeasonsMediaRatingSource(RatingSource.TRAKT)
      .assertUiState(
        DetailsPreferencesUiState(
          movieSource = RatingSource.TMDB,
          tvSource = RatingSource.TMDB,
          episodesSource = RatingSource.TMDB,
          seasonsSource = RatingSource.TRAKT,
        ),
      )
  }
}
