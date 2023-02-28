package com.andreolas.movierama.details.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeGetMoviesDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.ui.components.details.similar.SIMILAR_MOVIES_SCROLLABLE_LIST
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun navigateToAnotherDetailsScreen() {

        val getMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
        val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
        val destinationsNavigator = FakeDestinationsNavigator()

        destinationsNavigator.navigate(
            direction = DetailsScreenDestination(
                DetailsNavArguments(movieId = 0, isFavorite = false)
            )
        )

        getMovieDetailsUseCase.mockResultMovieDetails(
            result = flowOf(
                Result.Success(
                    MovieDetailsResult.DetailsSuccess(
                        movieDetails = movieDetails,
                    )
                ),
                Result.Success(
                    MovieDetailsResult.SimilarSuccess(
                        similar = similarMovies,
                    )
                )
            )
        )

        composeTestRule.setContent {
            DetailsScreen(navigator = destinationsNavigator,
                viewModel = DetailsViewModel(
                    getMovieDetailsUseCase = getMovieDetailsUseCase,
                    onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
                    savedStateHandle = SavedStateHandle(
                        mapOf(
                            "movieId" to 0,
                            "isFavorite" to false,
                        )
                    )
                )
            )
        }

        composeTestRule
            .onNodeWithTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
            .performScrollToNode(
                matcher = hasText(
                    similarMovies[0].title
                )
            )

        composeTestRule
            .onNodeWithTag(SIMILAR_MOVIES_SCROLLABLE_LIST)
            .performScrollToNode(
                matcher = hasText(similarMovies[8].title)
            )

        composeTestRule
            .onNodeWithText(similarMovies[8].title)
            .assertIsDisplayed()
            .performClick()

        destinationsNavigator.verifyNavigatedToDirection(
            expectedDirection = DetailsScreenDestination(
                DetailsNavArguments(movieId = 9, isFavorite = false)
            )
        )

        val navigateUpContentDescription = composeTestRule.activity
            .getString(R.string.navigate_up_button_content_description)

        composeTestRule
            .onNodeWithContentDescription(navigateUpContentDescription)
            .performClick()

        destinationsNavigator.verifyNavigatedToDirection(
            expectedDirection = DetailsScreenDestination(
                DetailsNavArguments(movieId = 0, isFavorite = false)
            )
        )
    }

    private val similarMovies = (1..10).map {
        SimilarMovie(
            id = it,
            posterPath = "",
            releaseDate = "",
            title = "Flight Club $it",
            rating = "",
            overview = "This movie is good.",
        )
    }.toList()
}

internal val movieDetails = MovieDetails(
    id = 1123,
    posterPath = "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
    releaseDate = "2022",
    title = "Flight Club",
    rating = "9.5",
    isFavorite = false,
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a " +
        "shocking new form of therapy. Their concept catches on, with underground fight clubs forming in every town," +
        " until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    director = Director(id = 123443321, name = "Forest Gump", profilePath = "BoxOfChocolates.jpg"),
    cast = listOf(
        Actor(
            id = 1,
            name = "Jack",
            profilePath = "AllWorkAndNoPlay.jpg",
            character = "HelloJohnny",
            order = 0
        ),
        Actor(
            id = 2,
            name = "Nicholson",
            profilePath = "Cuckoo.jpg",
            character = "McMurphy",
            order = 1
        ),
        Actor(
            id = 3,
            name = "Jack",
            profilePath = "AllWorkAndNoPlay.jpg",
            character = "HelloJohnny",
            order = 0
        ),
        Actor(
            id = 4,
            name = "Nicholson",
            profilePath = "Cuckoo.jpg",
            character = "McMurphy",
            order = 1
        ),
    ),
    genres = listOf("Thriller", "Drama", "Comedy", "Mystery", "Fantasy"),
    runtime = "2h 10m",
)
