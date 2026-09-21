package com.divinelink.core.model.track.backup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonExport(
  val id: Long,
  @SerialName("path") val profilePath: String?,
  val name: String,
  val gender: Int,
  @SerialName("dept") val knownForDepartment: String?,
  val savedAt: String,
)
