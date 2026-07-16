package com.divinelink.scenepeek.di

import com.divinelink.core.domain.details.media.AddToAccountUseCase
import com.divinelink.core.domain.details.media.DeleteRatingUseCase
import com.divinelink.core.domain.details.media.FetchAccountMediaDetailsUseCase
import com.divinelink.core.domain.details.media.GetMediaDetailsUseCase
import com.divinelink.core.domain.details.media.SubmitRatingUseCase
import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appUseCaseModule = module {

  factoryOf(::FetchMultiInfoSearchUseCase)

  // Details
  factoryOf(::AddToAccountUseCase)
  factoryOf(::DeleteRatingUseCase)
  factoryOf(::GetMediaDetailsUseCase)
  factoryOf(::FetchAccountMediaDetailsUseCase)
  factoryOf(::SubmitRatingUseCase)
}
