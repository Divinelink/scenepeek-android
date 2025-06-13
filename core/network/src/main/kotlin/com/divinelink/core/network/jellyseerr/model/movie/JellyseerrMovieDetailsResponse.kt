package com.divinelink.core.network.jellyseerr.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrMovieDetailsResponse(val mediaInfo: MovieInfoResponse? = null)
