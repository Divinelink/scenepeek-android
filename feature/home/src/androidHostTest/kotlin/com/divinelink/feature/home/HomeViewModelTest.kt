package com.divinelink.feature.home

import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.feature.home.HomeFormFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.time.Clock

@Suppress("LargeClass")
class HomeViewModelTest {

  private val testRobot = HomeViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val sectionData = MediaItemFactory.paginationData()

  @Test
  fun `successful initialise viewModel`() = runTest {
    testRobot
      .mockFetchSectionData(Result.success(MediaItemFactory.emptyPaginationData()))
      .buildViewModel()
      .assertUiState(
        createUiState(
          forms = createForms(
            trending = HomeFormFactory.empty,
            popularMovies = HomeFormFactory.empty,
            upcomingMovies = HomeFormFactory.empty,
            popularTV = HomeFormFactory.empty,
            upcomingTV = HomeFormFactory.empty,
          ),
          pages = sections.associateWith { 1 },
        ),
      )
  }

//  private fun loadData(
//    starting: Int,
//    ending: Int,
//  ): PaginationData<MediaItem> = MediaItemFactory.paginationData().copy(
//    list = (starting..ending).map {
//      MediaItemFactory.FightClub().toWizard {
//        withId(it)
//      }
//    },
//  )

  private fun createUiState(
    clock: Clock = ClockFactory.decemberFirst2021(),
    forms: Map<HomeSection, HomeForm<MediaItem>>,
    pages: Map<HomeSection, Int>,
  ) = HomeUiState.initial(
    sections = buildHomeSections(clock),
  ).copy(
    forms = forms,
    pages = pages,
  )

  private val trendingSection = HomeSection.TrendingAll
  private val popularMoviesSection = HomeSection.Popular(MediaType.MOVIE)
  private val upcomingMoviesSection = HomeSection.Upcoming(
    mediaType = MediaType.MOVIE,
    minDate = "2021-12-01",
  )
  private val popularTVSection = HomeSection.Popular(MediaType.TV)
  private val upcomingTVSection = HomeSection.Upcoming(
    mediaType = MediaType.TV,
    minDate = "2021-12-01",
  )

  val sections = listOf(
    trendingSection,
    popularMoviesSection,
    upcomingMoviesSection,
    popularTVSection,
    upcomingTVSection,
  )

  private fun createForms(
    trending: HomeForm<MediaItem>,
    popularMovies: HomeForm<MediaItem>,
    upcomingMovies: HomeForm<MediaItem>,
    popularTV: HomeForm<MediaItem>,
    upcomingTV: HomeForm<MediaItem>,
  ): Map<HomeSection, HomeForm<MediaItem>> = mapOf(
    trendingSection to trending,
    popularMoviesSection to popularMovies,
    upcomingMoviesSection to upcomingMovies,
    popularTVSection to popularTV,
    upcomingTVSection to upcomingTV,
  )
}
