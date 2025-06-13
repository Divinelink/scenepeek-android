package com.divinelink.core.model.jellyseerr.media

sealed class JellyseerrMediaInfo(open val status: JellyseerrMediaStatus) {

  data class Movie(override val status: JellyseerrMediaStatus) : JellyseerrMediaInfo(status)

  data class TV(
    override val status: JellyseerrMediaStatus,
    val seasons: Map<Int, JellyseerrMediaStatus>,
  ) : JellyseerrMediaInfo(status)
}
