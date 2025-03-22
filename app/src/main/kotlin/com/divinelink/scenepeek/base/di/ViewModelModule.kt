package com.divinelink.scenepeek.base.di

import com.divinelink.feature.credits.ui.CreditsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.person.ui.PersonViewModel
import com.divinelink.feature.onboarding.ui.OnboardingViewModel
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsViewModel
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.details.DetailsPreferencesViewModel
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.feature.watchlist.WatchlistViewModel
import com.divinelink.scenepeek.MainViewModel
import com.divinelink.scenepeek.home.ui.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
  viewModelOf(::AccountSettingsViewModel)
  viewModelOf(::AppearanceSettingsViewModel)
  viewModelOf(::CreditsViewModel)
  viewModelOf(::DetailsViewModel)
  viewModelOf(::HomeViewModel)
  viewModelOf(::MainViewModel)
  viewModelOf(::PersonViewModel)
  viewModelOf(::JellyseerrSettingsViewModel)
  viewModelOf(::WatchlistViewModel)
  viewModelOf(::DetailsPreferencesViewModel)
  viewModelOf(::TMDBAuthViewModel)
  viewModelOf(::OnboardingViewModel)
}
