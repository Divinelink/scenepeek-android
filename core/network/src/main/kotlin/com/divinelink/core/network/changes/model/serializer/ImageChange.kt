package com.divinelink.core.network.changes.model.serializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageChange(@SerialName("file_path") val filePath: String)
