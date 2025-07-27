package com.divinelink.core.network.list.model.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateListRequest(
  val name: String,
  val description: String,
  val public: Boolean,
  @SerialName("backdrop_path") val backdropPath: String?,
) {
  companion object {
    fun create(
      name: String,
      description: String,
      public: Boolean,
      backdrop: String?,
    ): UpdateListRequest = UpdateListRequest(
      name = name,
      description = description,
      public = public,
      backdropPath = backdrop,
    )
  }
}
