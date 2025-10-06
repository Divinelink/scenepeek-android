package com.divinelink.feature.discover.filters

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Language

data class SelectFilterUiState(
  val loading: Boolean,
  val genres: List<Genre>,
  val languages: List<Language>,
  val selectedGenres: List<Genre>,
  val selectedLanguages: List<Language>,
) {
  companion object {
    val initial = SelectFilterUiState(
      loading = true,
      genres = emptyList(),
      selectedGenres = emptyList(),
      languages = Language.entries,
      selectedLanguages = emptyList(),
    )
  }
}
