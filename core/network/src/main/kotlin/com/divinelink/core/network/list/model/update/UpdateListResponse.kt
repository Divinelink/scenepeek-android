package com.divinelink.core.network.list.model.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateListResponse(
  val success: Boolean,
  @SerialName("status_code") val statusCode: Int,
)
