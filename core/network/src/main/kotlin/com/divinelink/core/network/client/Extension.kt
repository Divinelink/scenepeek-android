package com.divinelink.core.network.client

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

inline fun <reified T> JsonElement.decode(): T = localJson.decodeFromJsonElement<T>(this)
