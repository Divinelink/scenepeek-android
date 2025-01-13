package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage

class TestMediaRatingPreferenceUseCase {

  private val mainDispatcherRule = MainDispatcherRule()

  fun mock(
    movieRatingSource: RatingSource = RatingSource.TMDB,
    tvRatingSource: RatingSource = RatingSource.TMDB,
    episodesRatingSource: RatingSource = RatingSource.TMDB,
    seasonsRatingSource: RatingSource = RatingSource.TMDB,
  ) = MediaRatingPreferenceUseCase(
    storage = FakePreferenceStorage(
      movieRatingSource = movieRatingSource,
      tvRatingSource = tvRatingSource,
      episodesRatingSource = episodesRatingSource,
      seasonsRatingSource = seasonsRatingSource,
    ),
    dispatcher = mainDispatcherRule.testDispatcher,
  )
}
