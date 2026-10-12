@file:Suppress("ComplexCondition")

package com.divinelink.core.database.migration

import com.divinelink.core.commons.Constants
import com.divinelink.core.commons.extensions.toEpochSeconds
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.model.track.backup.LibraryExport
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class LibraryMigrationManager(private val clockProvider: ClockProvider) {
  fun migrateAndParse(jsonString: String): LibraryExport {
    // 1. Parse just the version first to decide what to do
    val jsonElement = Json.parseToJsonElement(jsonString)
    val currentVersion = jsonElement.jsonObject["version"]?.jsonPrimitive?.int ?: 1

    var mutableJson = jsonElement.jsonObject.toMutableMap()

    if (currentVersion < 2) {
      mutableJson = migrateV1ToV2(mutableJson)
    }

    // Add future migrations here:
    // if (currentVersion < 3) { mutableJson = migrateV2ToV3(mutableJson) }

    mutableJson["version"] = JsonPrimitive(Constants.LIBRARY_EXPORT_VERSION)

    // 4. Convert back to string and decode into the final Data Class
    val finalJsonString = JsonObject(mutableJson).toString()
    return Json.decodeFromString<LibraryExport>(finalJsonString)
  }

  private fun migrateV1ToV2(
    data: MutableMap<String, JsonElement>,
  ): MutableMap<String, JsonElement> {
    val migratedRoot = migrateWatchedDates(
      rootJson = JsonObject(data),
    )

    data.clear()
    data.putAll(migratedRoot)

    data["version"] = JsonPrimitive(2)

    return data
  }

  /**
   * V1 Helpers
   */
  private fun migrateWatchedDates(rootJson: JsonObject): JsonObject {
    val showsArray = rootJson["shows"]?.jsonArray ?: return rootJson

    val migratedShows = showsArray.map { showElement ->
      val showObj = showElement.jsonObject.toMutableMap()

      val watchedArray = showObj["watched"]?.jsonArray
      if (watchedArray != null) {
        val migratedWatched = watchedArray.mapNotNull { watchedItem ->
          val watchedObj = watchedItem.jsonObject
          val dateValue = watchedObj["watchedAt"]

          if (dateValue == null || dateValue is JsonNull ||
            (dateValue is JsonPrimitive && !dateValue.isString)
          ) {
            return@mapNotNull null
          }

          try {
            val dateString = (dateValue as JsonPrimitive).content
            val epochSeconds = LocalDate.parse(dateString).toEpochSeconds(clockProvider.timezone)

            JsonObject(
              watchedObj.toMutableMap().apply {
                this["watchedAt"] = JsonPrimitive(epochSeconds)
              },
            )
          } catch (_: Exception) {
            println("Skipping invalid watchedAt: $dateValue")
            null
          }
        }

        showObj["watched"] = JsonArray(migratedWatched)
      }

      JsonObject(showObj)
    }

    return JsonObject(
      rootJson.toMutableMap().apply {
        this["shows"] = JsonArray(migratedShows)
      },
    )
  }
  /**
   * End V1 Helpers
   */
}
