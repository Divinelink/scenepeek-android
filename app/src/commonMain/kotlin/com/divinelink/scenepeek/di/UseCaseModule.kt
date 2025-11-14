package com.divinelink.scenepeek.di

import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import com.divinelink.feature.details.media.usecase.AddToWatchlistUseCase
import com.divinelink.feature.details.media.usecase.DeleteRatingUseCase
import com.divinelink.feature.details.media.usecase.FetchAccountMediaDetailsUseCase
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import com.divinelink.feature.details.media.usecase.SubmitRatingUseCase
import com.divinelink.scenepeek.home.usecase.GetFavoriteMoviesUseCase
import com.divinelink.scenepeek.home.usecase.GetPopularMoviesUseCase
import com.divinelink.scenepeek.home.usecase.GetSearchMoviesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appUseCaseModule = module {

  factoryOf(::FetchMultiInfoSearchUseCase)
  factoryOf(::GetFavoriteMoviesUseCase)
  factoryOf(::GetPopularMoviesUseCase)
  factoryOf(::GetSearchMoviesUseCase)

  // Details
  factoryOf(::AddToWatchlistUseCase)
  factoryOf(::DeleteRatingUseCase)
  factoryOf(::GetMediaDetailsUseCase)
  factoryOf(::FetchAccountMediaDetailsUseCase)
  factoryOf(::SubmitRatingUseCase)
}
