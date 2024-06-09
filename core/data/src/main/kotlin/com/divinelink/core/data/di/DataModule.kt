package com.divinelink.core.data.di

import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
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
}
