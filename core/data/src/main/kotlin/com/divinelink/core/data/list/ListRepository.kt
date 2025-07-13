package com.divinelink.core.data.list

import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.ListDetails

interface ListRepository {

  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult>

  suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Result<ListDetails>
}
