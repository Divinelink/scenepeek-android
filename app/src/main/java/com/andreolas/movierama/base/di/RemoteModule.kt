package com.andreolas.movierama.base.di

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.media.service.ProdMediaService
import com.divinelink.core.network.session.service.ProdSessionService
import com.divinelink.core.network.session.service.SessionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module is responsible for defining the creation of any Remote Services dependencies used
 * in the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

  @Singleton
  @Provides
  fun provideMediaService(
    restClient: RestClient,
  ): MediaService = ProdMediaService(restClient)

  @Singleton
  @Provides
  fun provideSessionService(
    restClient: RestClient,
  ): SessionService = ProdSessionService(restClient)
}
