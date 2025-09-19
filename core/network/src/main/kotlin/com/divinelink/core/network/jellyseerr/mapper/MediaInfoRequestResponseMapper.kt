package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.localizeIsoDate
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse

fun List<MediaInfoRequestResponse>?.map() = this?.map { it.map() } ?: emptyList()

fun MediaInfoRequestResponse.map() = JellyseerrRequest(
  id = id,
  jellyseerrMediaId = media.id,
  media = MediaReference(
    mediaId = media.tmdbId,
    mediaType = MediaType.from(media.mediaType),
  ),
  mediaStatus = JellyseerrStatus.Media.from(media.status),
  requestStatus = JellyseerrStatus.Request.from(status),
  requester = requestedBy.map(),
  seasons = seasons.map {
    SeasonRequest(
      seasonNumber = it.seasonNumber,
      status = JellyseerrStatus.Media.from(it.status),
    )
  },
  requestDate = createdAt.localizeIsoDate(),
  profileName = profileName,
  canRemove = canRemove == true,
)
