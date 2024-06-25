package com.divinelink.core.network.di

import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.account.ProdAccountService
import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.jellyseerr.service.ProdJellyseerrService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

  @Singleton
  @Provides
  fun provideAccountService(restClient: RestClient): AccountService = ProdAccountService(restClient)

  @Singleton
  @Provides
  fun provideJellyseerrService(restClient: RestClient): JellyseerrService =
    ProdJellyseerrService(restClient)
}
