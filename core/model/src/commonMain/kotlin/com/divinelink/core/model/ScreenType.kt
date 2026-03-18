package com.divinelink.core.model

sealed class ScreenType(
  open val id: Int,
  open val name: String,
) {
  data class Movie(
    override val id: Int,
    override val name: String,
  ) : ScreenType(id = id, name = name)

  data class Show(
    override val id: Int,
    override val name: String,
    val spoilersObfuscated: Boolean,
  ) : ScreenType(id = id, name = name)

  data class Collection(
    override val id: Int,
    override val name: String,
  ) : ScreenType(id = id, name = name)

  data class Person(
    override val id: Int,
    override val name: String,
  ) : ScreenType(id = id, name = name)

  data class List(
    override val id: Int,
    override val name: String,
  ) : ScreenType(id = id, name = name)

  data class Season(
    override val id: Int,
    override val name: String,
    val seasonNumber: Int,
  ) : ScreenType(id = id, name = name)

  data class Episode(
    override val id: Int,
    override val name: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
  ) : ScreenType(id = id, name = name)

  data object Unknown : ScreenType(
    id = -1,
    name = "",
  )
}

fun ScreenType.shareUrl(
  domain: String,
  name: String,
): String {
  val scheme = "https://www.$domain"
  val urlName = name
    .lowercase()
    .replace(":", "")
    .replace(regex = "[\\s|/]".toRegex(), replacement = "-")

  return when (this) {
    is ScreenType.Collection -> "$scheme/collection/$id-$urlName"
    is ScreenType.List -> "$scheme/list/$id-$urlName"
    is ScreenType.Movie -> "$scheme/movie/$id-$urlName"
    is ScreenType.Person -> "$scheme/person/$id-$urlName"
    is ScreenType.Show -> "$scheme/tv/$id-$urlName"
    is ScreenType.Season -> "$scheme/tv/$id-$urlName/season/$seasonNumber"
    is ScreenType.Episode -> "$scheme/tv/$id-$urlName/season/$seasonNumber/episode/$episodeNumber"
    ScreenType.Unknown -> ""
  }
}
