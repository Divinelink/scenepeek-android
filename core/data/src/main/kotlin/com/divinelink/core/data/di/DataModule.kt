package com.divinelink.core.data.di

import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.data.network.ConnectivityManagerNetworkMonitor
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {

  singleOf(::ProdAccountRepository) { bind<AccountRepository>() }
  singleOf(::ProdJellyseerrRepository) { bind<JellyseerrRepository>() }
  singleOf(::ProdPersonRepository) { bind<PersonRepository>() }

  singleOf(::ConnectivityManagerNetworkMonitor) { bind<NetworkMonitor>() }
}
