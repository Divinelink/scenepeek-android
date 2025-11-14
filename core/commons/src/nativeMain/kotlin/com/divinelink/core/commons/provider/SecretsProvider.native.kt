package com.divinelink.core.commons.provider

class NativeSecretsProvider(
  override val tmdbUrl: String = "BuildConfig.TMDB_BASE_URL", // Todo
  override val tmdbAuth: String = "BuildConfig.TMDB_AUTH_TOKEN", // Todo
  override val traktApiKey: String = "BuildConfig.TRAKT_API_KEY", // Todo
  override val omdbApiKey: String = "BuildConfig.OMDB_API_KEY", // Todo
) : SecretProvider

actual fun getConfigProvider(): SecretProvider = NativeSecretsProvider()
