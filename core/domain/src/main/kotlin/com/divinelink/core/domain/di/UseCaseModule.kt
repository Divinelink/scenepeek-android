package com.divinelink.core.domain.di

import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.domain.details.media.FetchAllRatingsUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {

  factoryOf(::FindByIdUseCase)
  factoryOf(::FetchChangesUseCase)
  // Credits
  factoryOf(::FetchCreditsUseCase)

  // Details - Person
  factoryOf(::FetchPersonDetailsUseCase)
  // Details - Media
  factoryOf(::FetchAllRatingsUseCase)

  // Jellyseerr
  factoryOf(::GetJellyseerrAccountDetailsUseCase)
  factoryOf(::LoginJellyseerrUseCase)
  factoryOf(::LogoutJellyseerrUseCase)
  factoryOf(::RequestMediaUseCase)

  // Session
  factoryOf(::CreateSessionUseCase)
  factoryOf(::LogoutUseCase)
  factoryOf(::ObserveSessionUseCase)
  factoryOf(::ObserveAccountUseCase)

  factoryOf(::CreateRequestTokenUseCase)
  factoryOf(::FetchWatchlistUseCase)
  factoryOf(::GetAccountDetailsUseCase)
  factoryOf(::GetDropdownMenuItemsUseCase)
  factoryOf(::GetDetailsActionItemsUseCase)
  factoryOf(::MarkAsFavoriteUseCase)

  factoryOf(::SpoilersObfuscationUseCase)

  // Settings
  factoryOf(::MediaRatingPreferenceUseCase)
}
