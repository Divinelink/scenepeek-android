package com.divinelink.core.model.certification

import com.divinelink.core.model.UIText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Certification(
  val certification: String,
  @Contextual val meaning: UIText,
  val order: Int,
)
