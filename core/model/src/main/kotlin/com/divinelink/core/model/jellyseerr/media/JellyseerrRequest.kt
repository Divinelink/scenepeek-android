package com.divinelink.core.model.jellyseerr.media

sealed class JellyseerrRequest(
  open val id: Int,
  open val status: JellyseerrMediaStatus,
  open val requester: JellyseerrRequester,
) {
  data class Movie(
    override val id: Int,
    override val status: JellyseerrMediaStatus,
    override val requester: JellyseerrRequester,
  ) : JellyseerrRequest(
    id = id,
    status = status,
    requester = requester,
  )

  data class TV(
    override val id: Int,
    override val status: JellyseerrMediaStatus,
    override val requester: JellyseerrRequester,
    val seasons: List<Int>,
  ) : JellyseerrRequest(
    id = id,
    status = status,
    requester = requester,
  )
}
