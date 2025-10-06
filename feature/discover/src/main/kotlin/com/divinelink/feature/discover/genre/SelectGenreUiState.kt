package com.divinelink.feature.discover.genre

import com.divinelink.core.model.Genre

data class SelectGenreUiState(
  val loading: Boolean,
  val genres: List<Genre>,
  val selectedGenres: List<Genre>,
) {
  companion object {
    val initial = SelectGenreUiState(
      loading = true,
      genres = emptyList(),
      selectedGenres = emptyList(),
    )
  }
}
