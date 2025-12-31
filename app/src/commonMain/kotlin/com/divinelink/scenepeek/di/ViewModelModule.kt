package com.divinelink.scenepeek.di

import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.feature.add.to.account.list.AddToListViewModel
import com.divinelink.feature.add.to.account.modal.ActionMenuViewModel
import com.divinelink.feature.credits.ui.CreditsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.person.ui.PersonViewModel
import com.divinelink.feature.discover.DiscoverViewModel
import com.divinelink.feature.discover.filters.SelectFilterViewModel
import com.divinelink.feature.home.HomeViewModel
import com.divinelink.feature.lists.create.CreateListViewModel
import com.divinelink.feature.lists.create.backdrop.SelectBackdropViewModel
import com.divinelink.feature.lists.details.ListDetailsViewModel
import com.divinelink.feature.lists.user.ListsViewModel
import com.divinelink.feature.onboarding.ui.IntroViewModel
import com.divinelink.feature.profile.ProfileViewModel
import com.divinelink.feature.request.media.RequestMediaViewModel
import com.divinelink.feature.requests.RequestsViewModel
import com.divinelink.feature.search.ui.SearchViewModel
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsViewModel
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.details.DetailsPreferencesViewModel
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.feature.user.data.UserDataViewModel
import com.divinelink.scenepeek.MainViewModel
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
  viewModelOf(::SearchViewModel)
  viewModelOf(::JellyseerrSettingsViewModel)
  viewModelOf(::UserDataViewModel)
  viewModelOf(::DetailsPreferencesViewModel)
  viewModelOf(::TMDBAuthViewModel)
  viewModelOf(::IntroViewModel)
  viewModelOf(::ProfileViewModel)
  viewModelOf(::ListsViewModel)
  viewModelOf(::AddToListViewModel)
  viewModelOf(::ListDetailsViewModel)
  viewModelOf(::CreateListViewModel)
  viewModelOf(::SelectBackdropViewModel)
  viewModelOf(::ActionMenuViewModel)
  viewModelOf(::RequestMediaViewModel)
  viewModelOf(::RequestsViewModel)
  viewModelOf(::DiscoverViewModel)
  viewModelOf(::SelectFilterViewModel)

  // Components
  viewModelOf(::SwitchViewButtonViewModel)
}
