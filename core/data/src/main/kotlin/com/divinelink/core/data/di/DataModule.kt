package com.divinelink.core.data.di

import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

  @Binds
  internal abstract fun bindsAccountRepository(
    accountRepository: ProdAccountRepository,
  ): AccountRepository

  @Binds
  internal abstract fun bindsJellyseerrRepository(
    jellyseerrRepository: ProdJellyseerrRepository,
  ): JellyseerrRepository

  @Binds
  internal abstract fun bindsPersonRepository(repository: ProdPersonRepository): PersonRepository
}
