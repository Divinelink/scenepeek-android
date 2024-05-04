package com.andreolas.movierama.ui.theme

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@Suppress("UNUSED")
abstract class ThemedActivityDelegateModule {

  @Singleton
  @Binds
  abstract fun provideThemedActivityDelegate(
    impl: ThemedActivityDelegateImpl
  ): ThemedActivityDelegate
}
