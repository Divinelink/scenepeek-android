package com.andreolas.movierama.base.data.remote.movies.dto.account.states

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = RateResponseApiSerializer::class)
sealed class RateResponseApi {

  @Serializable
  @SerialName("value")
  data class Value(val value: Float) : RateResponseApi()

  @Serializable
  data object False : RateResponseApi()
}
