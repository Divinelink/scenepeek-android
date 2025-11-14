package com.divinelink.core.network.list.model.add

import com.divinelink.core.network.list.model.MediaItemRequest
import kotlinx.serialization.Serializable

@Serializable
data class AddToListRequest(val items: List<MediaItemRequest>)
