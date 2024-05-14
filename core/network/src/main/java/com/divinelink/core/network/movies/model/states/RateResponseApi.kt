package com.divinelink.core.network.movies.model.states

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
