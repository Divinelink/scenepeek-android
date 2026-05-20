package com.divinelink.core.model.config

data class ConfigMessage(
  val id: String,
  val content: String,
  val visible: Boolean,
  val dismissable: Boolean,
)
