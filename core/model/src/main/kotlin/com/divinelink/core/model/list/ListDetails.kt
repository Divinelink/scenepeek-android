package com.divinelink.core.model.list

import com.divinelink.core.model.media.MediaItem

data class ListDetails(
  val id: Int,
  val page: Int,
  val name: String,
  val description: String,
  val backdropPath: String?,
  val media: List<MediaItem.Media>,
  val totalPages: Int,
  val totalResults: Int,
  val public: Boolean,
  val averageRating: Double,
  val posterPath: String?,
  val itemCount: Int,
  val revenue: Long,
  val runtime: Int,
  val sortBy: String,
  val iso31661: String,
  val iso6391: String,
  val createdBy: CreatedByUser,
) {
  fun canLoadMore(): Boolean = page < totalPages
  fun isEmpty(): Boolean = media.isEmpty()
}
