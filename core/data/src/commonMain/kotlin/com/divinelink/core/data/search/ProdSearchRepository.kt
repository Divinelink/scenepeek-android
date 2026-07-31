package com.divinelink.core.data.search

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class ProdSearchRepository(
  private val mediaDao: MediaDao,
  private val personDao: PersonDao,
) : SearchRepository {

  override fun fetchSearchHistory(): Flow<List<MediaItem>> = mediaDao
    .fetchSearchHistory()
    .flatMapLatest { history ->
      val mediaIds = history.filter { MediaType.isMedia(it.mediaType) }.map { it.mediaId }
      val personIds = history.filter { !MediaType.isMedia(it.mediaType) }.map { it.mediaId }

      val mediaFlow = mediaDao.selectByIds(mediaIds)
      val personFlow = personDao.selectByIds(personIds)

      combine(
        mediaFlow,
        personFlow,
      ) { media, persons ->
        val mediaMap = media.associateBy { it.uniqueIdentifier }
        val personMap = persons.associateBy { it.id }

        history.mapNotNull { entity ->
          if (MediaType.isMedia(entity.mediaType)) {
            val key = "${entity.mediaType}-${entity.mediaId}"
            mediaMap[key]
          } else {
            personMap[entity.mediaId]
          }
        }
      }
    }

  override fun addToSearchHistory(media: MediaReference) {
    mediaDao.addToSearchHistory(media)
  }

  override fun removeFromHistory(media: MediaReference) {
    mediaDao.removeFromSearchHistory(media)
  }

  override fun clearSearchHistory() {
    mediaDao.clearSearchHistory()
  }
}
