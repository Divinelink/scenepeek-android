package com.divinelink.core.network.list.mapper.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.media.model.search.multi.mapper.mapToMedia

fun ListDetailsResponse.map() = ListDetails(
  page = page,
  backdropPath = backdropPath,
  totalPages = totalPages,
  totalResults = totalResults,
  name = name,
  media = results.mapToMedia(),
  description = description,
  public = public,
  id = id,
  averageRating = averageRating,
  posterPath = posterPath,
  itemCount = itemCount,
  revenue = revenue,
  runtime = runtime,
  sortBy = sortBy,
  iso31661 = iso31661,
  iso6391 = iso6391,
  createdBy = createdBy.map(),
)
