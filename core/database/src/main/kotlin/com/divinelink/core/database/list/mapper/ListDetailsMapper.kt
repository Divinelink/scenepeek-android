package com.divinelink.core.database.list.mapper

import com.divinelink.core.database.list.ListDetailsEntity
import com.divinelink.core.model.list.ListDetails

fun ListDetails.map() = ListDetailsEntity(
  id = id.toLong(),
  name = name,
  description = description,
  averageRating = averageRating,
  backdropPath = backdropPath,
  posterPath = posterPath,
  itemCount = itemCount.toLong(),
  page = page.toLong(),
  totalPages = totalPages.toLong(),
  totalResults = totalResults.toLong(),
  isPublic = if (public) 1 else 0,
  revenue = revenue,
  runtime = runtime.toLong(),
  sortBy = sortBy,
  iso31661 = iso31661,
  iso6391 = iso6391,
  createdById = createdBy.id,
  createdByUsername = createdBy.username,
  createdByName = createdBy.name,
  createdByAvatarPath = createdBy.avatarPath,
  createdByGravatarHash = createdBy.gravatarHash,
  commentsJson = null,
  objectIdsJson = null,
)
