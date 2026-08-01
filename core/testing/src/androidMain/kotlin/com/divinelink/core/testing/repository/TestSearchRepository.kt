package com.divinelink.core.testing.repository

import com.divinelink.core.data.search.SearchRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestSearchRepository {
  val mock: SearchRepository = mock()

  init {
    mockFetchSearchHistory(flowOf(emptyList()))
  }

  fun mockFetchSearchHistory(response: Flow<List<MediaItem>>) {
    whenever(mock.fetchSearchHistory()).thenReturn(response)
  }

  fun verifyAddToHistory(mediaReference: MediaReference) {
    verify(mock).addToSearchHistory(mediaReference)
  }
}
