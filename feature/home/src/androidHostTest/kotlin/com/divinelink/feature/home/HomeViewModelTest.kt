package com.divinelink.feature.home

import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class HomeViewModelTest {

  private val testRobot = HomeViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `successful initialise viewModel`() = runTest {
    testRobot
      .buildViewModel()
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries,
        ),
      )
  }

  @Test
  fun `test updating home sections visibility`() = runTest {
    testRobot
      .buildViewModel()
      .assertUiState(
        createUiState(sections = MediaListSection.homeEntries),
      )
      .toggleHomeSectionVisibility(section = MediaListSection.Upcoming(MediaType.MOVIE))
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries.minus(
            MediaListSection.Upcoming(MediaType.MOVIE),
          ),
        ),
      )
      .toggleHomeSectionVisibility(section = MediaListSection.RecentRequests)
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries
            .minus(MediaListSection.Upcoming(MediaType.MOVIE))
            .minus(MediaListSection.RecentRequests),
        ),
      )
      .toggleHomeSectionVisibility(section = MediaListSection.Upcoming(MediaType.MOVIE))
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries
            .minus(MediaListSection.RecentRequests),
        ),
      )
  }

  private fun createUiState(
    sections: List<MediaListSection.Home>,
  ) = HomeUiState.initial.copy(
    sections = sections.buildSections(),
  )
}
