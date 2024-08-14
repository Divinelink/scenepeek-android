package com.divinelink.core.network.di

import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.account.ProdAccountService
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.PersistentCookieStorage
import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.details.person.service.PersonService
import com.divinelink.core.network.details.person.service.ProdPersonService
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.jellyseerr.service.ProdJellyseerrService
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val remoteModule = module {

  single<HttpClientEngine> { Android.create() }

  single<JellyseerrRestClient> {
    val engine: HttpClientEngine = get()
    val storage: EncryptedStorage = get()

    JellyseerrRestClient(engine, storage)
  }

  single<AccountService> {
    val restClient: RestClient = get()

    ProdAccountService(restClient)
  }

  single<JellyseerrService> {
    val restClient: JellyseerrRestClient = get()

    ProdJellyseerrService(restClient)
  }

  single<PersonService> {
    val restClient: RestClient = get()

    ProdPersonService(restClient)
  }

  single<PersistentCookieStorage> {
    val encryptedStorage: EncryptedStorage = get()

    PersistentCookieStorage(encryptedStorage)
  }
}
