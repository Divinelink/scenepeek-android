package com.divinelink.core.testing.commons.provider

import com.divinelink.core.commons.provider.SecretProvider

class TestSecretProvider : SecretProvider {
  override val tmdbUrl = "tmdbUrl"
  override val tmdbAuth = "tmdbAuth"
  override val traktApiKey = "traktApiKey"
  override val omdbApiKey = "omdbApiKey"
}
