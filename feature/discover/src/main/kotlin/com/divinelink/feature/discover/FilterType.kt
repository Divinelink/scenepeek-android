package com.divinelink.feature.discover

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface FilterType {
  val options: List<Any>
  val selectedOptions: List<Any>?
  val query: String?
  val visibleOptions: List<Any>

  data class Genres(
    override val options: List<Genre>,
    override val selectedOptions: List<Genre>,
    override val query: String?,
  ) : FilterType {
    override val visibleOptions: List<Genre>
      get() = query?.let {
        options.filter { it.name.contains(other = query, ignoreCase = true) }
      } ?: options
  }

  data class Languages(
    override val options: List<Language>,
    override val selectedOptions: List<Language>,
    override val query: String?,
  ) : FilterType {
    override val visibleOptions: List<Language>
      get() = query?.let {
        options.filter { it.name.contains(other = query, ignoreCase = true) }
      } ?: options
  }

  data class Countries(
    override val options: List<Country>,
    override val selectedOptions: List<Country>,
    override val query: String?,
  ) : FilterType {
    override val visibleOptions: List<Country>
      get() = query?.let {
        options.filter { it.name.contains(other = query, ignoreCase = true) }
      } ?: options
  }
}
