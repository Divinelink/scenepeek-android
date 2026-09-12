package com.divinelink.core.testing.repository

import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.database.show.ShowSyncError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestTrackRepository {

  val mock: TrackRepository = mock()

  init {
    mockGetSyncErrors(response = flowOf(emptyList()))
  }

  fun mockGetSyncErrors(response: Flow<List<ShowSyncError>>) {
    whenever(mock.getSyncErrors()).thenReturn(response)
  }
}
