package com.divinelink.core.network.session.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsResponseApi(
  val id: Int,
  val username: String,
  val name: String,
)
