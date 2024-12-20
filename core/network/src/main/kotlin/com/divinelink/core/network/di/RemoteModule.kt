package com.divinelink.core.network.di

import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.account.ProdAccountService
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.PersistentCookieStorage
import com.divinelink.core.network.details.person.service.PersonService
import com.divinelink.core.network.details.person.service.ProdPersonService
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.jellyseerr.service.ProdJellyseerrService
import com.divinelink.core.network.omdb.service.OMDbService
import com.divinelink.core.network.omdb.service.ProdOMDbService
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteModule = module {

  single<HttpClientEngine> { Android.create() }

  singleOf(::JellyseerrRestClient) { bind<JellyseerrRestClient>() }

  singleOf(::ProdAccountService) { bind<AccountService>() }

  singleOf(::ProdJellyseerrService) { bind<JellyseerrService>() }

  singleOf(::ProdPersonService) { bind<PersonService>() }

  singleOf(::ProdOMDbService) { bind<OMDbService>() }

  singleOf(::PersistentCookieStorage) { bind<PersistentCookieStorage>() }
}
