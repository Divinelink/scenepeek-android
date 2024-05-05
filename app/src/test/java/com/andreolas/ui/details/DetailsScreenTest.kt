package com.andreolas.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.andreolas.ComposeTest
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.ui.DetailsNavArguments
import com.andreolas.movierama.details.ui.DetailsScreen
import com.andreolas.movierama.details.ui.DetailsViewModel
import com.andreolas.movierama.details.ui.MOVIE_DETAILS_SCROLLABLE_LIST_TAG
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeGetMoviesDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.ui.components.details.similar.SIMILAR_MOVIES_SCROLLABLE_LIST
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class DetailsScreenTest : ComposeTest() {

  @Test
  fun navigateToAnotherDetailsScreen() {

    val getMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          mediaType = MediaType.MOVIE.name,
          isFavorite = false,
        )
      )
    )

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(
        Result.success(AccountMediaDetailsFactory.Rated())
      )
    )

    getMovieDetailsUseCase.mockFetchMovieDetails(
      response = flowOf(
        Result.success(
          MovieDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub()
          )
        ),
        Result.success(
          MovieDetailsResult.SimilarSuccess(
            similar = MediaItemFactory.MoviesList(),
          )
        )
      )
    )

    composeTestRule.setContent {
      DetailsScreen(
        navigator = destinationsNavigator,
        viewModel = DetailsViewModel(
          getMovieDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          fetchAccountMediaDetailsUseCase = fetchAccountMediaDetailsUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 0,
              "isFavorite" to false,
              "mediaType" to MediaType.MOVIE.value,
            )
          )
        )
      )
    }

    composeTestRule
      .onNodeWithTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
      .performScrollToNode(
        matcher = hasText(
          MediaItemFactory.MoviesList()[0].name
        )
      )

    composeTestRule
      .onNodeWithTag(SIMILAR_MOVIES_SCROLLABLE_LIST)
      .performScrollToNode(
        matcher = hasText(MediaItemFactory.MoviesList()[0].name)
      )

    composeTestRule
      .onNodeWithText(MediaItemFactory.MoviesList()[0].name)
      .assertIsDisplayed()
      .performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      expectedDirection = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          isFavorite = false,
          mediaType = MediaType.MOVIE.name,
        )
      )
    )

    val navigateUpContentDescription = composeTestRule.activity
      .getString(R.string.navigate_up_button_content_description)

    composeTestRule
      .onNodeWithContentDescription(navigateUpContentDescription)
      .performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      expectedDirection = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          isFavorite = false,
          mediaType = MediaType.MOVIE.name,
        )
      )
    )
  }
}
