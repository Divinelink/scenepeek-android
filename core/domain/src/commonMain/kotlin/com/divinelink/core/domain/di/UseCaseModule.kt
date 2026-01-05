package com.divinelink.core.domain.di

import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.DiscoverMediaUseCase
import com.divinelink.core.domain.FetchUserDataUseCase
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
import com.divinelink.core.domain.jellyseerr.DeleteMediaUseCase
import com.divinelink.core.domain.jellyseerr.DeleteRequestUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.jellyseerr.GetServerInstanceDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetServerInstancesUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.domain.list.AddItemToListUseCase
import com.divinelink.core.domain.list.CreateListUseCase
import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.domain.list.FetchUserListsUseCase
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.domain.theme.GetAvailableColorPreferencesUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.domain.theme.GetThemeUseCase
import com.divinelink.core.domain.theme.ObserveThemeModeUseCase
import com.divinelink.core.domain.theme.ProdSystemThemeProvider
import com.divinelink.core.domain.theme.SetColorPreferenceUseCase
import com.divinelink.core.domain.theme.SetThemeUseCase
import com.divinelink.core.domain.theme.SystemThemeProvider
import com.divinelink.core.domain.theme.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.black.backgrounds.ObserveBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.color.GetCustomColorUseCase
import com.divinelink.core.domain.theme.color.ObserveColorPreferencesUseCase
import com.divinelink.core.domain.theme.color.ObserveCustomColorUseCase
import com.divinelink.core.domain.theme.material.you.MaterialYouProvider
import com.divinelink.core.domain.theme.material.you.ProdMaterialYouProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
  singleOf(::ProdSystemThemeProvider) { bind<SystemThemeProvider>() }
  singleOf(::ProdMaterialYouProvider) { bind<MaterialYouProvider>() }

  factoryOf(::SetThemeUseCase)
  factoryOf(::GetThemeUseCase)
  factoryOf(::GetAvailableThemesUseCase)
  factoryOf(::SetColorPreferenceUseCase)
  factoryOf(::GetCustomColorUseCase)
  factoryOf(::GetAvailableColorPreferencesUseCase)
  factoryOf(::SetBlackBackgroundsUseCase)
  factoryOf(::GetBlackBackgroundsUseCase)
  factoryOf(::ObserveBlackBackgroundsUseCase)
  factoryOf(::ObserveThemeModeUseCase)
  factoryOf(::ObserveColorPreferencesUseCase)
  factoryOf(::ObserveCustomColorUseCase)

  factoryOf(::FindByIdUseCase)
  factoryOf(::FetchChangesUseCase)
  // Credits
  factoryOf(::FetchCreditsUseCase)

  // Details - Person
  factoryOf(::FetchPersonDetailsUseCase)
  // Details - Media
  factoryOf(::FetchAllRatingsUseCase)

  // Jellyseerr
  factoryOf(::GetJellyseerrProfileUseCase)
  factoryOf(::LoginJellyseerrUseCase)
  factoryOf(::LogoutJellyseerrUseCase)
  factoryOf(::RequestMediaUseCase)
  factoryOf(::DeleteRequestUseCase)
  factoryOf(::DeleteMediaUseCase)
  factoryOf(::GetServerInstancesUseCase)
  factoryOf(::GetServerInstanceDetailsUseCase)

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
  factoryOf(::DiscoverMediaUseCase)

  factoryOf(::SpoilersObfuscationUseCase)

  // Settings
  factoryOf(::MediaRatingPreferenceUseCase)

  // Onboarding
  factoryOf(::MarkOnboardingCompleteUseCase)

  // Lists
  factoryOf(::AddItemToListUseCase)
  factoryOf(::FetchListDetailsUseCase)
  factoryOf(::CreateListUseCase)

  singleOf(::SearchStateManager)
}
