package com.divinelink.core.commons.provider

expect fun getConfigProvider(): SecretProvider

interface SecretProvider {
  val tmdbUrl: String
  val tmdbAuth: String
  val traktApiKey: String
  val omdbApiKey: String
}
