package com.divinelink.feature.settings.app.details

import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestMediaRatingPreferenceUseCase
import kotlinx.coroutines.flow.Flow

class DetailsPreferencesViewModelTestRobot : ViewModelTestRobot<DetailsPreferencesUiState>() {

  private lateinit var viewModel: DetailsPreferencesViewModel

  private val mediaRatingPreferenceUseCase = TestMediaRatingPreferenceUseCase()

  private var movieRatingSource = RatingSource.TMDB
  private var tvRatingSource = RatingSource.TMDB
  private var episodesRatingSource = RatingSource.TMDB
  private var seasonsRatingSource = RatingSource.TMDB

  override fun buildViewModel() = apply {
    viewModel = DetailsPreferencesViewModel(
      mediaRatingPreferenceUseCase = mediaRatingPreferenceUseCase.mock(
        movieRatingSource = movieRatingSource,
        tvRatingSource = tvRatingSource,
        episodesRatingSource = episodesRatingSource,
        seasonsRatingSource = seasonsRatingSource,
      ),
    )
  }

  fun mockTvRatingSource(source: RatingSource) = apply {
    tvRatingSource = source
  }

  fun mockMovieRatingSource(source: RatingSource) = apply {
    movieRatingSource = source
  }

  fun onSetMovieMediaRatingSource(source: RatingSource) = apply {
    viewModel.setMediaRatingSource(MediaRatingSource.Movie to source)
  }

  fun onSetTvMediaRatingSource(source: RatingSource) = apply {
    viewModel.setMediaRatingSource(MediaRatingSource.TVShow to source)
  }

  fun onSetEpisodesMediaRatingSource(source: RatingSource) = apply {
    viewModel.setMediaRatingSource(MediaRatingSource.Episodes to source)
  }

  fun onSetSeasonsMediaRatingSource(source: RatingSource) = apply {
    viewModel.setMediaRatingSource(MediaRatingSource.Seasons to source)
  }

  override val actualUiState: Flow<DetailsPreferencesUiState>
    get() = viewModel.uiState
}
