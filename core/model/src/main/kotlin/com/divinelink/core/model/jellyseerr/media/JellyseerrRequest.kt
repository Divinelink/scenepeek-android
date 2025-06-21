package com.divinelink.core.model.jellyseerr.media

sealed class JellyseerrRequest(
  open val id: Int,
  open val status: JellyseerrMediaStatus,
  open val requester: JellyseerrRequester,
  open val requestDate: String,
) {
  data class Movie(
    override val id: Int,
    override val status: JellyseerrMediaStatus,
    override val requester: JellyseerrRequester,
    override val requestDate: String,
  ) : JellyseerrRequest(
    id = id,
    status = status,
    requester = requester,
    requestDate = requestDate,
  )

  data class TV(
    override val id: Int,
    override val status: JellyseerrMediaStatus,
    override val requester: JellyseerrRequester,
    override val requestDate: String,
    val seasons: List<Int>,
  ) : JellyseerrRequest(
    id = id,
    status = status,
    requester = requester,
    requestDate = requestDate,
  )
}
