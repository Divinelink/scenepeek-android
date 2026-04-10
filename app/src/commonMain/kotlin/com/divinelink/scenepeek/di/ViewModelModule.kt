package com.divinelink.scenepeek.di

import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.ui.menu.DropdownMenuViewModel
import com.divinelink.feature.add.to.account.list.AddToListViewModel
import com.divinelink.feature.add.to.account.modal.ActionMenuViewModel
import com.divinelink.feature.collections.CollectionsViewModel
import com.divinelink.feature.credits.ui.CreditsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.person.ui.PersonViewModel
import com.divinelink.feature.discover.DiscoverViewModel
import com.divinelink.feature.discover.filters.SelectFilterViewModel
import com.divinelink.feature.episode.EpisodeViewModel
import com.divinelink.feature.home.HomeViewModel
import com.divinelink.feature.lists.create.CreateListViewModel
import com.divinelink.feature.lists.create.backdrop.SelectBackdropViewModel
import com.divinelink.feature.lists.details.ListDetailsViewModel
import com.divinelink.feature.lists.user.ListsViewModel
import com.divinelink.feature.media.lists.MediaListsViewModel
import com.divinelink.feature.onboarding.ui.IntroViewModel
import com.divinelink.feature.profile.ProfileViewModel
import com.divinelink.feature.request.media.RequestMediaViewModel
import com.divinelink.feature.requests.RequestsViewModel
import com.divinelink.feature.search.ui.SearchViewModel
import com.divinelink.feature.season.SeasonViewModel
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsViewModel
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.details.DetailsPreferencesViewModel
import com.divinelink.feature.settings.app.updates.AppUpdatesViewModel
import com.divinelink.feature.tmdb.auth.TMDBAuthViewModel
import com.divinelink.feature.updater.UpdaterViewModel
import com.divinelink.feature.user.data.UserDataViewModel
import com.divinelink.scenepeek.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
  viewModelOf(::AccountSettingsViewModel)
  viewModelOf(::AppearanceSettingsViewModel)
  viewModel { params -> CollectionsViewModel(params.get(), get()) }
  viewModel { params -> CreditsViewModel(params.get(), get(), get()) }
  viewModel { params -> DetailsViewModel(params.get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
  viewModelOf(::HomeViewModel)
  viewModelOf(::MainViewModel)
  viewModel { params -> PersonViewModel(params.get(), get(), get()) }
  viewModelOf(::SearchViewModel)
  viewModelOf(::JellyseerrSettingsViewModel)
  viewModel { params -> UserDataViewModel(params.get(), get(), get(), get()) }
  viewModelOf(::DetailsPreferencesViewModel)
  viewModelOf(::TMDBAuthViewModel)
  viewModelOf(::IntroViewModel)
  viewModelOf(::ProfileViewModel)
  viewModelOf(::ListsViewModel)
  viewModel { params -> AddToListViewModel(params.get(), get(), get(), get()) }
  viewModel { params -> ListDetailsViewModel(params.get(), get(), get()) }
  viewModel { params -> CreateListViewModel(params.get(), get(), get()) }
  viewModelOf(::SelectBackdropViewModel)
  viewModelOf(::ActionMenuViewModel)
  viewModelOf(::RequestMediaViewModel)
  viewModelOf(::RequestsViewModel)
  viewModel { params -> DiscoverViewModel(params.get(), get(), get(), get()) }
  viewModelOf(::SelectFilterViewModel)
  viewModel { params -> MediaListsViewModel(params.get(), get()) }
  viewModel { params -> SeasonViewModel(params.get(), get()) }
  viewModel { params -> EpisodeViewModel(params.get(), get(), get()) }
  viewModelOf(::UpdaterViewModel)
  viewModelOf(::AppUpdatesViewModel)

  // Components
  viewModelOf(::SwitchViewButtonViewModel)
  viewModelOf(::DropdownMenuViewModel)
}
