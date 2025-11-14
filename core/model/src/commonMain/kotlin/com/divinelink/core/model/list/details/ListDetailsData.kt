package com.divinelink.core.model.list.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.media.MediaItem

sealed class ListDetailsData<out T>(
  open val name: String,
  open val backdropPath: String?,
  open val description: String,
  open val public: Boolean,
) {
  data class Initial(
    override val name: String,
    override val backdropPath: String?,
    override val description: String,
    override val public: Boolean,
  ) : ListDetailsData<Nothing>(
    name = name,
    backdropPath = backdropPath,
    description = description,
    public = public,
  )

  data class Data(
    val data: ListDetails,
    val pages: Map<Int, List<MediaItem.Media>>,
  ) : ListDetailsData<ListDetails>(
    name = data.name,
    backdropPath = data.backdropPath,
    description = data.description,
    public = data.public,
  ) {
    val media = pages.values.flatten()
    val isEmpty: Boolean = media.isEmpty()
    val canLoadMore: Boolean = data.canLoadMore()
  }
}
