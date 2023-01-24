package com.andreolas.movierama.base.di

import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import com.andreolas.movierama.base.data.remote.movies.service.ProdMovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module is responsible for defining the creation of any Remote Services dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideMovieService(restClient: RestClient): MovieService =
        ProdMovieService(restClient)
}
