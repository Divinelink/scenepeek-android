package com.divinelink.core.domain.di

import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.domain.HandleAuthenticationRequestUseCase
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import org.koin.dsl.module

val useCaseModule = module {

  // Credits
  factory { FetchCreditsUseCase(get(), get()) }

  // Details - Person
  factory { FetchPersonDetailsUseCase(get(), get()) }

  // Jellyseerr
  factory { GetJellyseerrDetailsUseCase(get(), get(), get()) }
  factory { LoginJellyseerrUseCase(get(), get(), get()) }
  factory { LogoutJellyseerrUseCase(get(), get(), get()) }
  factory { RequestMediaUseCase(get(), get(), get()) }

  // Session
  factory { CreateSessionUseCase(get(), get(), get()) }
  factory { LogoutUseCase(get(), get(), get()) }
  factory { ObserveSessionUseCase(get(), get()) }

  factory { CreateRequestTokenUseCase(get(), get(), get()) }
  factory { FetchWatchlistUseCase(get(), get(), get()) }
  factory { GetAccountDetailsUseCase(get(), get(), get()) }
  factory { GetDropdownMenuItemsUseCase(get(), get()) }
  factory { HandleAuthenticationRequestUseCase(get(), get(), get(), get()) }
  factory { MarkAsFavoriteUseCase(get(), get()) }
}
