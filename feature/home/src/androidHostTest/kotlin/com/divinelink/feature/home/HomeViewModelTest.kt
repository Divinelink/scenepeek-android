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
      .setSeerrEnabled(true)
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
      .setSeerrEnabled(true)
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

  @Test
  fun `test with with enabled section recent requests is hidden`() = runTest {
    testRobot
      .buildViewModel()
      .setSeerrEnabled(false)
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries.minus(MediaListSection.RecentRequests),
        ),
      )
      .setSeerrEnabled(true)
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries,
        ),
      )
  }

  @Test
  fun `test reorder home sections`() = runTest {
    testRobot
      .buildViewModel()
      .setSeerrEnabled(true)
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries,
        ),
      )
      .updateSectionsOrder(
        sections = listOf(
          MediaListSection.Popular(MediaType.MOVIE),
          MediaListSection.TrendingAll,
          MediaListSection.Popular(MediaType.TV),
          MediaListSection.RecentRequests,
          MediaListSection.Upcoming(MediaType.MOVIE),
          MediaListSection.Upcoming(MediaType.TV),
        ),
      )
      .assertUiState(
        createUiState(
          sections = listOf(
            MediaListSection.Popular(MediaType.MOVIE),
            MediaListSection.TrendingAll,
            MediaListSection.Popular(MediaType.TV),
            MediaListSection.RecentRequests,
            MediaListSection.Upcoming(MediaType.MOVIE),
            MediaListSection.Upcoming(MediaType.TV),
          ),
        ),
      )
  }

  @Test
  fun `test reorder home sections with initially seerr disabled`() = runTest {
    testRobot
      .buildViewModel()
      .setSeerrEnabled(false)
      .assertUiState(
        createUiState(
          sections = MediaListSection.homeEntries.minus(
            MediaListSection.RecentRequests,
          ),
        ),
      )
      .updateSectionsOrder(
        sections = listOf(
          MediaListSection.Popular(MediaType.MOVIE),
          MediaListSection.TrendingAll,
          MediaListSection.Popular(MediaType.TV),
          MediaListSection.Upcoming(MediaType.MOVIE),
          MediaListSection.Upcoming(MediaType.TV),
        ),
      )
      .assertUiState(
        createUiState(
          sections = listOf(
            MediaListSection.Popular(MediaType.MOVIE),
            MediaListSection.TrendingAll,
            MediaListSection.Popular(MediaType.TV),
            MediaListSection.Upcoming(MediaType.MOVIE),
            MediaListSection.Upcoming(MediaType.TV),
          ),
        ),
      )
      .setSeerrEnabled(true)
      .assertUiState(
        createUiState(
          sections = listOf(
            MediaListSection.Popular(MediaType.MOVIE),
            MediaListSection.TrendingAll,
            MediaListSection.Popular(MediaType.TV),
            MediaListSection.Upcoming(MediaType.MOVIE),
            MediaListSection.Upcoming(MediaType.TV),
            MediaListSection.RecentRequests,
          ),
        ),
      )
  }

  private fun createUiState(sections: List<MediaListSection.Home>) = HomeUiState.initial.copy(
    sections = sections.buildSections(),
  )
}
