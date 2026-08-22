package com.divinelink.feature.home

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.filter.HomeFilter
import com.divinelink.core.model.filter.SelectableFilter
import com.divinelink.core.model.home.HomeSectionInfo

@Immutable
data class HomeUiState(
  val filters: List<SelectableFilter>,
  val sections: List<HomeSectionInfo>,
) {
  companion object {
    val initial = HomeUiState(
      filters = HomeFilter.entries,
      sections = emptyList(),
    )
  }
}
