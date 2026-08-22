package com.divinelink.feature.home

import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class HomeViewModelTestRobot {

  private lateinit var viewModel: HomeViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val preferencesRepository = TestPreferencesRepository()

  fun buildViewModel() = apply {
    viewModel = HomeViewModel(
      preferencesRepository = preferencesRepository,
      searchStateManager = SearchStateManager(),
    )
  }

  fun assertUiState(expectedUiState: HomeUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home) = apply {
    preferencesRepository.toggleHomeSectionVisibility(section)
  }

  suspend fun updateSectionsOrder(sections: List<MediaListSection.Home>) = apply {
    preferencesRepository.updateSectionsOrder(sections)
  }
}
