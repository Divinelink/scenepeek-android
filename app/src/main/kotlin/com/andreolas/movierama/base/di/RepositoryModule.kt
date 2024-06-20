package com.andreolas.movierama.base.di

import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.details.repository.ProdDetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.data.session.repository.ProdSessionRepository
import com.divinelink.core.data.session.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for defining the creation of any repository dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun bindMovieRepository(moviesRepository: ProdMediaRepository): MediaRepository

  @Binds
  abstract fun bindDetailsRepository(moviesRepository: ProdDetailsRepository): DetailsRepository

  @Binds
  abstract fun bindSessionRepository(sessionRepository: ProdSessionRepository): SessionRepository
}
