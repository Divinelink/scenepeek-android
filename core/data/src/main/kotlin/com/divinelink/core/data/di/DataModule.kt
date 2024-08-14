package com.divinelink.core.data.di

import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import org.koin.dsl.module

val dataModule = module {
  single<AccountRepository> { ProdAccountRepository(remote = get()) }

  single<JellyseerrRepository> {
    ProdJellyseerrRepository(
      service = get(),
      queries = get(),
      dispatcher = get(),
    )
  }

  single<PersonRepository> {
    ProdPersonRepository(
      service = get(),
      dao = get(),
      clock = get(),
      dispatcher = get(),
    )
  }
}
