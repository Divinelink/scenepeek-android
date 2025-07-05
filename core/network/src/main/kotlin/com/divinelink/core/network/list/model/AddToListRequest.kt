package com.divinelink.core.network.list.model

import kotlinx.serialization.Serializable

@Serializable
data class AddToListRequest(val items: List<MediaItemRequest>)
