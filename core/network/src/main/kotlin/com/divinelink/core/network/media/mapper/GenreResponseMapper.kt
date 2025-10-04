package com.divinelink.core.network.media.mapper

import com.divinelink.core.model.Genre
import com.divinelink.core.network.media.model.GenreResponse
import com.divinelink.core.network.media.model.GenresListResponse

fun GenresListResponse.map(): List<Genre> = this.genres.map(GenreResponse::map)

fun GenreResponse.map(): Genre = Genre(
  id = id,
  name = name,
)
