package com.andreolas.movierama.base.di

import com.andreolas.movierama.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.andreolas.movierama.home.domain.usecase.FetchMultiInfoSearchUseCase
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.divinelink.feature.details.media.usecase.AddToWatchlistUseCase
import com.divinelink.feature.details.media.usecase.DeleteRatingUseCase
import com.divinelink.feature.details.media.usecase.FetchAccountMediaDetailsUseCase
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import com.divinelink.feature.details.media.usecase.SubmitRatingUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appUseCaseModule = module {

  factoryOf(::SetRemoteConfigUseCase)
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
