package com.divinelink.scenepeek.home.ui

import com.divinelink.core.model.media.MediaItem

data class MediaSection(
  val data: List<MediaItem>,
  val shouldLoadMore: Boolean,
) {
  fun addMore(more: List<MediaItem>) = copy(
    data = getUpdatedMedia(
      currentMediaList = data,
      updatedMediaList = more,
    ),
  )

  /**
   * Update the current list of media items with the updated list of media items.
   * We need this to get the latest favorite status of each media item.
   */
  private fun getUpdatedMedia(
    currentMediaList: List<MediaItem>,
    updatedMediaList: List<MediaItem>,
  ): List<MediaItem> {
    val combinedList = currentMediaList.plus(updatedMediaList).distinctBy { it.id }
    val updatedList = combinedList.toMutableList()
    updatedMediaList.forEach { updatedMovie ->
      val index = updatedList.indexOfFirst { it.id == updatedMovie.id }
      if (index != -1) {
        updatedList[index] = updatedMovie
      }
    }
    return updatedList.distinctBy { it.id }
  }
}
