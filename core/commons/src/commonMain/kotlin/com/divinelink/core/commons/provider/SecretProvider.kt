package com.divinelink.core.commons.provider

interface SecretProvider {
  val tmdbUrl: String
  val tmdbAuth: String
  val traktApiKey: String
  val omdbApiKey: String
}

expect fun getConfigProvider(): SecretProvider
