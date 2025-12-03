package com.divinelink.core.database.list.mapper

import com.divinelink.core.database.list.ListDetailsEntity
import com.divinelink.core.model.list.CreatedByUser
import com.divinelink.core.model.list.ListDetails

fun ListDetailsEntity?.map(): ListDetails? {
  if (this == null) return null

  return ListDetails(
    id = id.toInt(),
    page = page.toInt(),
    name = name,
    description = description ?: "",
    backdropPath = backdropPath,
    media = emptyList(),
    totalPages = totalPages.toInt(),
    totalResults = totalResults.toInt(),
    public = isPublic?.toInt() == 1,
    averageRating = averageRating,
    posterPath = posterPath,
    itemCount = itemCount.toInt(),
    revenue = revenue,
    runtime = runtime.toInt(),
    sortBy = sortBy,
    iso31661 = iso31661,
    iso6391 = iso6391,
    createdBy = CreatedByUser(
      id = createdById,
      name = createdByName ?: "",
      username = createdByUsername,
      avatarPath = createdByAvatarPath,
      gravatarHash = createdByGravatarHash,
    ),
  )
}
