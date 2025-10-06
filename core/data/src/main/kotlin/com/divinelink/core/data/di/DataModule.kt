package com.divinelink.core.data.di

import com.divinelink.core.data.GenreRepository
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.auth.ProdAuthRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.data.list.ProdListRepository
import com.divinelink.core.data.network.ConnectivityManagerNetworkMonitor
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.data.preferences.ProdPreferencesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {

  singleOf(::ProdAccountRepository) { bind<AccountRepository>() }
  singleOf(::ProdListRepository) { bind<ListRepository>() }
  singleOf(::ProdJellyseerrRepository) { bind<JellyseerrRepository>() }
  singleOf(::ProdPersonRepository) { bind<PersonRepository>() }
  singleOf(::ProdPreferencesRepository) { bind<PreferencesRepository>() }
  singleOf(::ProdAuthRepository) { bind<AuthRepository>() }
  singleOf(::GenreRepository)

  singleOf(::ConnectivityManagerNetworkMonitor) { bind<NetworkMonitor>() }
}
