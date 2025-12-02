package com.divinelink.core.commons.provider

import platform.Foundation.NSBundle

class NativeSecretsProvider(
  override val tmdbUrl: String =
    NSBundle.mainBundle.infoDictionary?.get("TMDBBaseURL") as? String ?: "",
  override val tmdbAuth: String =
    NSBundle.mainBundle.infoDictionary?.get("TMDBAuthToken") as? String ?: "",
  override val traktApiKey: String =
    NSBundle.mainBundle.infoDictionary?.get("TraktAPIKey") as? String ?: "",
  override val omdbApiKey: String =
    NSBundle.mainBundle.infoDictionary?.get("OMDBAPIKey") as? String ?: "",
) : SecretProvider

actual fun getConfigProvider(): SecretProvider = NativeSecretsProvider()
