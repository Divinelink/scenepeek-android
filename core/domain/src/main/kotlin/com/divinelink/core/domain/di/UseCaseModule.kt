package com.divinelink.core.domain.di

import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.FetchUserDataUseCase
import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.account.FetchUserListsUseCase
import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.domain.details.media.FetchAllRatingsUseCase
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.jellyseerr.DeleteMediaUseCase
import com.divinelink.core.domain.jellyseerr.DeleteRequestUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.domain.list.AddItemToListUseCase
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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
  factoryOf(::DeleteRequestUseCase)
  factoryOf(::DeleteMediaUseCase)

  // Session
  factoryOf(::CreateSessionUseCase)
  factoryOf(::LogoutUseCase)
  factoryOf(::ObserveAccountUseCase)

  factoryOf(::CreateRequestTokenUseCase)
  factoryOf(::FetchUserDataUseCase)
  factoryOf(::FetchUserListsUseCase)
  factoryOf(::GetAccountDetailsUseCase)
  factoryOf(::GetDropdownMenuItemsUseCase)
  factoryOf(::GetDetailsActionItemsUseCase)
  factoryOf(::MarkAsFavoriteUseCase)

  factoryOf(::SpoilersObfuscationUseCase)

  // Settings
  factoryOf(::MediaRatingPreferenceUseCase)

  // Onboarding
  factoryOf(::MarkOnboardingCompleteUseCase)

  // ListsA
  factoryOf(::AddItemToListUseCase)

  singleOf(::SearchStateManager)
}
