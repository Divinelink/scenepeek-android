package com.andreolas.movierama.base.data.remote.session.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsResponseApi(
  val id: Int,
  val username: String,
  val name: String,
)
