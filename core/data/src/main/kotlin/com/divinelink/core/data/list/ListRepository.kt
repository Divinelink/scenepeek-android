package com.divinelink.core.data.list

import com.divinelink.core.model.list.AddToListResult

interface ListRepository {

  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult>
}
