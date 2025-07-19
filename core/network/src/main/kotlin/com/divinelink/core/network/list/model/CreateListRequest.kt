package com.divinelink.core.network.list.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateListRequest(
  val name: String,
  val description: String,
  val public: Boolean,
)
