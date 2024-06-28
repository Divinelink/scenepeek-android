package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @param address Is the address of the Jellyseerr server. It is not part of the body,
 * but it is used to build the URL, therefore it is Transient.
 */
@Serializable
data class JellyseerrRequestMediaBodyApi(
  @Transient val address: String = "",
  val mediaType: String,
  val mediaId: Int,
  val is4k: Boolean,
)
