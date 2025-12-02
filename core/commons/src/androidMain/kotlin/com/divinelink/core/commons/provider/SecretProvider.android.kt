package com.divinelink.core.commons.provider

import com.divinelink.core.commons.BuildConfig

class AndroidSecretsProvider(
  override val tmdbUrl: String = BuildConfig.TMDB_BASE_URL,
  override val tmdbAuth: String = BuildConfig.TMDB_AUTH_TOKEN,
  override val traktApiKey: String = BuildConfig.TRAKT_API_KEY,
  override val omdbApiKey: String = BuildConfig.OMDB_API_KEY,
) : SecretProvider

actual fun getConfigProvider(): SecretProvider = AndroidSecretsProvider()
