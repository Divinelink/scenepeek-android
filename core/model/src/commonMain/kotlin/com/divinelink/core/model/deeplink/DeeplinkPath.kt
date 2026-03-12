package com.divinelink.core.model.deeplink

sealed interface DeeplinkPath {
  data class Movie(val id: Long) : DeeplinkPath
  data class TV(val id: Long) : DeeplinkPath
  data class Person(
    val id: Long,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val profilePath: String? = null,
    val gender: Int? = null,
  ) : DeeplinkPath

  data class Collection(val id: Long) : DeeplinkPath
  data class List(val id: Long) : DeeplinkPath

  data class Episode(
    val showId: Long,
    val episodeNumber: Int,
    val seasonNumber: Int,
  ) : DeeplinkPath

  data class Season(
    val showId: Long,
    val seasonNumber: Int,
  ) : DeeplinkPath
}

fun String?.extractRouteFromDeeplink(): DeeplinkPath? {
  if (this.isNullOrBlank()) return null

  val path = normalizeUrl(this) ?: return null

  val pattern = Regex(
    "^/(movie|tv|collection|list|person)/(\\d+)(?:-[^/]+)?",
//    (?:/season/(\\d+)(?:/episode/(\\d+))?)?$",
    RegexOption.IGNORE_CASE,
  )

  return pattern.find(path)?.let { match ->
    val type = match.groupValues[1].lowercase()
    val id = match.groupValues[2].toLongOrNull() ?: return null

    // Temporarily disable seasons and episodes
    /*
    val season = match.groupValues[3].toIntOrNull()
    val episode = match.groupValues[4].toIntOrNull()
     */
    when {
//      season != null && episode != null && type == "tv" -> DeeplinkPath.Episode(
//        showId = id,
//        seasonNumber = season,
//        episodeNumber = episode,
//      )
//
//      season != null && type == "tv" -> DeeplinkPath.Season(
//        showId = id,
//        seasonNumber = season,
//      )

      else -> when (type) {
        "movie" -> DeeplinkPath.Movie(id)
        "tv" -> DeeplinkPath.TV(id)
        "collection" -> DeeplinkPath.Collection(id)
        "list" -> DeeplinkPath.List(id)
        "person" -> DeeplinkPath.Person(id)
        else -> null
      }
    }
  }
}

private fun normalizeUrl(input: String): String? {
  var cleaned = input
    .removePrefix("scenepeek://")
    .removePrefix("https://")
    .removePrefix("http://")
    .removePrefix("www.")

  val domains = listOf("scenepeek.app", "themoviedb.org")

  for (domain in domains) {
    if (cleaned.startsWith(domain, ignoreCase = true)) {
      cleaned = cleaned.substring(domain.length)
      break
    }
  }

  return if (cleaned.startsWith("/")) cleaned else "/$cleaned"
}
