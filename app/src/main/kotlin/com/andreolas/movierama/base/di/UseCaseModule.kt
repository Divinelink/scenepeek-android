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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appUseCaseModule = module {

  singleOf(::SetRemoteConfigUseCase)
  singleOf(::FetchMultiInfoSearchUseCase)
  singleOf(::GetFavoriteMoviesUseCase)
  singleOf(::GetPopularMoviesUseCase)
  singleOf(::GetSearchMoviesUseCase)

  // Details
  singleOf(::AddToWatchlistUseCase)
  singleOf(::DeleteRatingUseCase)
  singleOf(::GetMediaDetailsUseCase)
  singleOf(::FetchAccountMediaDetailsUseCase)
  singleOf(::SubmitRatingUseCase)
}
