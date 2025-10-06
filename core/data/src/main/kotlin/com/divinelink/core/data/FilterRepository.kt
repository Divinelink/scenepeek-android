package com.divinelink.core.data

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterRepository {
  private val _selectedGenres = MutableStateFlow<List<Genre>>(emptyList())
  val selectedGenres: StateFlow<List<Genre>> = _selectedGenres.asStateFlow()

  private val _selectedLanguage = MutableStateFlow<Language?>(null)
  val selectedLanguage: StateFlow<Language?> = _selectedLanguage.asStateFlow()

  fun updateSelectedGenres(genres: List<Genre>) {
    _selectedGenres.value = genres
  }

  fun updateLanguage(language: Language?) {
    _selectedLanguage.value = language
  }

  fun clear() {
    _selectedLanguage.value = null
    _selectedGenres.value = emptyList()
  }
}
