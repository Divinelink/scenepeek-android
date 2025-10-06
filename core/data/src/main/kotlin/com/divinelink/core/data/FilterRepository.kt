package com.divinelink.core.data

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterRepository {
  private val _selectedGenres = MutableStateFlow<List<Genre>>(emptyList())
  val selectedGenres: StateFlow<List<Genre>> = _selectedGenres.asStateFlow()

  private val _selectedLanguages = MutableStateFlow<List<Language>>(emptyList())
  val selectedLanguages: StateFlow<List<Language>> = _selectedLanguages.asStateFlow()

  fun updateSelectedGenres(genres: List<Genre>) {
    _selectedGenres.value = genres
  }

  fun updateLanguages(languages: List<Language>) {
    _selectedLanguages.value = languages
  }

  fun clear() {
    _selectedLanguages.value = emptyList()
    _selectedGenres.value = emptyList()
  }
}
