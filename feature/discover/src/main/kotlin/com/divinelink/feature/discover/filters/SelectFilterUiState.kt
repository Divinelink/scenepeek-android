package com.divinelink.feature.discover.filters

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.discover.FilterModal

data class SelectFilterUiState(
  val loading: Boolean,
  val error: BlankSlateState?,
  val mediaType: MediaType,
  val filterModal: FilterModal,
  val genres: List<Genre>,
  val languages: List<Language>,
  val countries: List<Country>,
  val selectedGenres: List<Genre>,
  val selectedLanguage: Language?,
  val selectedCountry: Country?,
) {
  companion object {
    fun initial(
      filterModal: FilterModal,
      mediaType: MediaType,
    ) = SelectFilterUiState(
      loading = true,
      error = null,
      mediaType = mediaType,
      filterModal = filterModal,
      genres = emptyList(),
      selectedGenres = emptyList(),
      languages = Language.entries,
      selectedLanguage = null,
      countries = Country.entries,
      selectedCountry = null,
    )
  }
}
