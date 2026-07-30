package com.divinelink.core.data.search

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

  fun fetchSearchHistory(): Flow<List<MediaItem>>

  fun addToSearchHistory(media: MediaReference)
}
