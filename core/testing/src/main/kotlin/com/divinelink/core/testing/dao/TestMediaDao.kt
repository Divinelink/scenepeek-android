package com.divinelink.core.testing.dao

import com.divinelink.core.database.media.dao.SqlMediaDao
import com.divinelink.core.model.media.MediaItem
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TestMediaDao {

  val mock: SqlMediaDao = mock()

  fun verifyItemInserted(item: MediaItem.Media) {
    verify(mock).insertMedia(item)
  }
}
