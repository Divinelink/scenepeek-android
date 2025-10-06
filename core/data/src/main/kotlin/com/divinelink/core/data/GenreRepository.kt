package com.divinelink.core.data

import com.divinelink.core.model.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GenreRepository {
  private val _selectedGenres = MutableStateFlow<Set<Genre>>(emptySet())
  val selectedGenres: StateFlow<Set<Genre>> = _selectedGenres.asStateFlow()

  fun updateSelectedGenres(genres: Set<Genre>) {
    _selectedGenres.value = genres
  }

  fun clear() {
    _selectedGenres.value = emptySet()
  }
}
