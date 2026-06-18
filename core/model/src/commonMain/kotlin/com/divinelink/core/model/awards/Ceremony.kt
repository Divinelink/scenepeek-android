package com.divinelink.core.model.awards

import kotlinx.serialization.Serializable

@Serializable
data class Ceremony(
  val id: String,
  val title: String,
  val imagePath: String,
)
