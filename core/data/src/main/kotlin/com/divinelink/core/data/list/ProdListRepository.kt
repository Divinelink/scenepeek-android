package com.divinelink.core.data.list

import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.network.list.mapper.map
import com.divinelink.core.network.list.service.ListService

class ProdListRepository(private val service: ListService) : ListRepository {

  override suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult> = service.addItemToList(
    listId = listId,
    mediaId = mediaId,
    mediaType = mediaType,
  ).map { it.map() }
}
