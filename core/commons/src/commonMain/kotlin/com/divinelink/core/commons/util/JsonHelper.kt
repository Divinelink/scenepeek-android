package com.divinelink.core.commons.util

import kotlinx.serialization.json.Json

class JsonHelper(val json: Json) {

  inline fun <reified T : Any> encodeToString(value: T): String = json.encodeToString(value)

  inline fun <reified T : Any> decodeFromString(jsonString: String): T? =
    runCatching { json.decodeFromString<T>(jsonString) }.getOrNull()
}
