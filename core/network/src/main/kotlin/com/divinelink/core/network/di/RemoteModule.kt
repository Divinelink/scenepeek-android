package com.divinelink.core.network.di

import com.divinelink.core.network.account.service.AccountService
import com.divinelink.core.network.account.service.ProdAccountService
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.OMDbClient
import com.divinelink.core.network.client.PersistentCookieStorage
import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.client.TraktClient
import com.divinelink.core.network.details.person.service.PersonService
import com.divinelink.core.network.details.person.service.ProdPersonService
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.jellyseerr.service.ProdJellyseerrService
import com.divinelink.core.network.omdb.service.OMDbService
import com.divinelink.core.network.omdb.service.ProdOMDbService
import com.divinelink.core.network.trakt.service.ProdTraktService
import com.divinelink.core.network.trakt.service.TraktService
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteModule = module {

  single<HttpClientEngine> { Android.create() }

  singleOf(::AuthTMDbClient) { bind<AuthTMDbClient>() }
  singleOf(::TMDbClient) { bind<TMDbClient>() }
  singleOf(::JellyseerrRestClient) { bind<JellyseerrRestClient>() }
  singleOf(::OMDbClient) { bind<OMDbClient>() }
  singleOf(::TraktClient) { bind<TraktClient>() }

  singleOf(::ProdAccountService) { bind<AccountService>() }

  singleOf(::ProdJellyseerrService) { bind<JellyseerrService>() }

  singleOf(::ProdPersonService) { bind<PersonService>() }

  singleOf(::ProdOMDbService) { bind<OMDbService>() }

  singleOf(::ProdTraktService) { bind<TraktService>() }

  singleOf(::PersistentCookieStorage) { bind<PersistentCookieStorage>() }
}
