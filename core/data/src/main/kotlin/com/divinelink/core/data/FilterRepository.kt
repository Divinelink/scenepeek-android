package com.divinelink.core.data

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterRepository {
  private val _selectedGenres = MutableStateFlow<List<Genre>>(emptyList())
  val selectedGenres: StateFlow<List<Genre>> = _selectedGenres.asStateFlow()

  private val _selectedLanguage = MutableStateFlow<Language?>(null)
  val selectedLanguage: StateFlow<Language?> = _selectedLanguage.asStateFlow()

  private val _selectedCountry = MutableStateFlow<Country?>(null)
  val selectedCountry: StateFlow<Country?> = _selectedCountry.asStateFlow()

  fun updateSelectedGenres(genres: List<Genre>) {
    _selectedGenres.value = genres
  }

  fun updateLanguage(language: Language?) {
    _selectedLanguage.value = language
  }

  fun updateCountry(country: Country?) {
    _selectedCountry.value = country
  }

  fun clear() {
    _selectedGenres.value = emptyList()
    _selectedLanguage.value = null
    _selectedCountry.value = null
  }
}
