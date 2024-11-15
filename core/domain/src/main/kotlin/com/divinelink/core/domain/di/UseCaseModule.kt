package com.divinelink.core.domain.di

import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.domain.HandleAuthenticationRequestUseCase
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.credits.EpisodesObfuscationUseCase
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {

  factoryOf(::FetchChangesUseCase)
  // Credits
  factoryOf(::FetchCreditsUseCase)

  // Details - Person
  factoryOf(::FetchPersonDetailsUseCase)

  // Jellyseerr
  factoryOf(::GetJellyseerrAccountDetailsUseCase)
  factoryOf(::LoginJellyseerrUseCase)
  factoryOf(::LogoutJellyseerrUseCase)
  factoryOf(::RequestMediaUseCase)

  // Session
  factoryOf(::CreateSessionUseCase)
  factoryOf(::LogoutUseCase)
  factoryOf(::ObserveSessionUseCase)

  factoryOf(::CreateRequestTokenUseCase)
  factoryOf(::FetchWatchlistUseCase)
  factoryOf(::GetAccountDetailsUseCase)
  factoryOf(::GetDropdownMenuItemsUseCase)
  factoryOf(::GetDetailsActionItemsUseCase)
  factoryOf(::HandleAuthenticationRequestUseCase)
  factoryOf(::MarkAsFavoriteUseCase)

  factoryOf(::EpisodesObfuscationUseCase)
}
