package com.divinelink.core.model.sort

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.first_air_date
import com.divinelink.core.model.resources.name
import com.divinelink.core.model.resources.popularity
import com.divinelink.core.model.resources.primary_release_date
import com.divinelink.core.model.resources.revenue
import com.divinelink.core.model.resources.title
import com.divinelink.core.model.resources.vote_average
import com.divinelink.core.model.resources.vote_count
import org.jetbrains.compose.resources.StringResource

enum class SortBy(val value: String, val label: StringResource) {
  NAME("name", Res.string.name),
  POPULARITY("popularity", Res.string.popularity),
  REVENUE("revenue", Res.string.revenue),
  RELEASE_DATE("primary_release_date", Res.string.primary_release_date),
  FIRST_AIR_DATE("first_air_date", Res.string.first_air_date),
  TITLE("title", Res.string.title),
  VOTE_AVERAGE("vote_average", Res.string.vote_average),
  VOTE_COUNT("vote_count", Res.string.vote_count),
  ;

  companion object {
    val discoverMovieEntries = listOf(
      POPULARITY,
      REVENUE,
      RELEASE_DATE,
      TITLE,
      VOTE_AVERAGE,
      VOTE_COUNT,
    )

    val discoverShowEntries = listOf(
      FIRST_AIR_DATE,
      NAME,
      POPULARITY,
      VOTE_AVERAGE,
      VOTE_COUNT,
    )

    fun findDiscoverMovieOption(value: String?): SortBy = discoverMovieEntries.find {
      it.value == value
    } ?: POPULARITY

    fun findDiscoverShowOption(value: String?): SortBy = discoverShowEntries.find {
      it.value == value
    } ?: POPULARITY

    fun from(value: String): SortBy? = entries.find { it.value == value }
  }
}
