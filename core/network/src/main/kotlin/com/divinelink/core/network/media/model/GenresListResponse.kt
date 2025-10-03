package com.divinelink.core.network.media.model

import kotlinx.serialization.Serializable

@Serializable
data class GenresListResponse(val genres: List<GenreResponse>)
