package com.divinelink.core.network.list.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListResponse(
  val id: Int,
  val success: Boolean,
  @SerialName("status_code") val statusCode: Int,
)
