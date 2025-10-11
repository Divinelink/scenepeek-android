package com.divinelink.feature.discover.filters

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface SelectFilterAction {
  data object ClearGenres : SelectFilterAction
  data object Retry : SelectFilterAction
  data class SelectGenre(val genre: Genre) : SelectFilterAction
  data class SelectLanguage(val language: Language) : SelectFilterAction
  data class SelectCountry(val country: Country) : SelectFilterAction
}
