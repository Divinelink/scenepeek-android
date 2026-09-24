package com.divinelink.core.model.sort

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.created_at
import com.divinelink.core.model.resources.first_air_date
import com.divinelink.core.model.resources.name
import com.divinelink.core.model.resources.original_order_sorting_filter
import com.divinelink.core.model.resources.popularity
import com.divinelink.core.model.resources.primary_release_date
import com.divinelink.core.model.resources.revenue
import com.divinelink.core.model.resources.title
import com.divinelink.core.model.resources.vote_average
import com.divinelink.core.model.resources.vote_count
import org.jetbrains.compose.resources.StringResource

sealed interface SortBy {
  val value: String
  val label: StringResource

  data object Popularity : SortBy {
    override val value = "popularity"
    override val label = Res.string.popularity
  }

  data object VoteAverage : SortBy {
    override val value = "vote_average"
    override val label = Res.string.vote_average
  }

  data object VoteCount : SortBy {
    override val value = "vote_count"
    override val label = Res.string.vote_count
  }

  data object Title : SortBy {
    override val value = "original_title"
    override val label = Res.string.title
  }

  data object Name : SortBy {
    override val value = "name"
    override val label = Res.string.name
  }

  data object ReleaseDate : SortBy {
    override val value = "release_date"
    override val label = Res.string.primary_release_date
  }

  data object FirstAirDate : SortBy {
    override val value = "first_air_date"
    override val label = Res.string.first_air_date
  }

  data object Revenue : SortBy {
    override val value = "revenue"
    override val label = Res.string.revenue
  }

  data object CreatedAt : SortBy {
    override val value = "created_at"
    override val label = Res.string.created_at
  }

  data object OriginalOrder : SortBy {
    override val value = "original_order"
    override val label = Res.string.original_order_sorting_filter
  }

  companion object {
    private val allValues by lazy {
      listOf(
        Popularity, VoteAverage, VoteCount, Title, Name,
        ReleaseDate, FirstAirDate, Revenue, CreatedAt, OriginalOrder,
      ).associateBy { it.value }
    }

    fun from(value: String?): SortBy? = value?.let { allValues[it] }
  }
}
