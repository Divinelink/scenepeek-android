package com.divinelink.core.testing.factories.core.commons

import com.divinelink.core.commons.util.JsonHelper
import kotlinx.serialization.json.Json

object JsonFactory {

  val mock = Json {
    prettyPrint = true
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
  }

  val jsonHelper = JsonHelper(mock)
}
