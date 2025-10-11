package com.divinelink.feature.discover.filters

import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.FilterType

data class SelectFilterUiState(
  val loading: Boolean,
  val error: BlankSlateState?,
  val mediaType: MediaType,
  val filterModal: FilterModal,
  val filterType: FilterType,
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
      filterType = when (filterModal) {
        FilterModal.Genre -> FilterType.Genres(
          options = emptyList(),
          selectedOptions = emptyList(),
          query = null,
        )
        FilterModal.Country -> FilterType.Countries(
          options = Country.entries,
          selectedOptions = emptyList(),
          query = null,
        )
        FilterModal.Language -> FilterType.Languages(
          options = Language.entries,
          selectedOptions = emptyList(),
          query = null,
        )
      },
    )
  }
}
