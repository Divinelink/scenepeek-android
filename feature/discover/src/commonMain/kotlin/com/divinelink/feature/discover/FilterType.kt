package com.divinelink.feature.discover

import com.divinelink.core.model.Genre
import com.divinelink.core.model.discover.YearType
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface FilterType {

  sealed interface Searchable : FilterType {
    val options: List<Any>
    val selectedOptions: List<Any>?
    val query: String?
    val visibleOptions: List<Any>

    data class Genres(
      override val options: List<Genre>,
      override val selectedOptions: List<Genre>,
      override val query: String?,
    ) : Searchable {
      override val visibleOptions: List<Genre>
        get() = query?.let {
          options.filter { it.name.contains(other = query, ignoreCase = true) }
        } ?: options
    }

    data class Languages(
      override val options: List<Language>,
      override val selectedOptions: List<Language>,
      override val query: String?,
    ) : Searchable {
      override val visibleOptions: List<Language>
        get() = query?.let {
          options.filter { it.name.contains(other = query, ignoreCase = true) }
        } ?: options
    }

    data class Countries(
      override val options: List<Country>,
      override val selectedOptions: List<Country>,
      override val query: String?,
    ) : Searchable {
      override val visibleOptions: List<Country>
        get() = query?.let {
          options.filter { it.name.contains(other = query, ignoreCase = true) }
        } ?: options
    }
  }

  data class VoteAverage(
    val greaterThan: Int,
    val lessThan: Int,
    val minimumVotes: Int,
  ) : FilterType

  sealed class Year(
    val type: YearType,
  ) : FilterType {
    data object Any : Year(type = YearType.Any)
    data class Decade(val decade: com.divinelink.core.model.Decade) : Year(type = YearType.Decade)
    data class Single(val year: Int) : Year(type = YearType.Single)
    data class Range(val startYear: Int, val endYear: Int) : Year(type = YearType.Range)
  }
}
