package com.andreolas.movierama.search.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.Credits
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.Genre
import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Cast
import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Crew
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.AuthorDetailsApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewResultsApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsResponseApi
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.details.domain.repository.ProdDetailsRepository
import com.andreolas.movierama.fakes.remote.FakeMovieRemote
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProdDetailsRepositoryTest {

    private val movieDetails = MovieDetails(
        id = 1123,
        posterPath = "123456",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "9.5",
        isFavorite = false,
        overview = "This movie is good.",
        director = Director(id = 123443321, name = "Forest Gump", profilePath = "BoxOfChocolates.jpg"),
        cast = listOf(
            Actor(id = 10, name = "Jack", profilePath = "AllWorkAndNoPlay.jpg", character = "HelloJohnny", order = 0),
            Actor(id = 20, name = "Nicholson", profilePath = "Cuckoo.jpg", character = "McMurphy", order = 1),
        ),
        genres = listOf("Thriller", "Drama", "Comedy"),
        runtime = "2h 10m",
        similarMovies = null,
        reviews = null,
    )

    private val detailsResponseApi = DetailsResponseApi(
        adult = false,
        backdropPath = "",
        belongToCollection = null,
        budget = 0,
        genres = listOf(
            Genre(id = 0, name = "Thriller"),
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Comedy"),
        ),
        homepage = null,
        id = 1123,
        imdbId = null,
        originalLanguage = "",
        originalTitle = "",
        overview = "This movie is good.",
        popularity = 0.0,
        posterPath = "123456",
        productionCompanies = listOf(),
        productionCountries = listOf(),
        releaseDate = "2022",
        revenue = 0,
        runtime = 130,
        spokenLanguage = listOf(),
        status = null,
        tagline = "",
        title = "Flight Club",
        video = false,
        voteAverage = 9.5,
        voteCount = 0,
        credits = Credits(
            cast = listOf(
                Cast(
                    adult = false,
                    castId = 10,
                    character = "HelloJohnny",
                    creditId = "",
                    gender = 0,
                    id = 10,
                    knownForDepartment = "",
                    name = "Jack",
                    order = 0,
                    originalName = "",
                    popularity = 0.0,
                    profilePath = "AllWorkAndNoPlay.jpg"
                ),
                Cast(
                    adult = false,
                    castId = 10,
                    character = "McMurphy",
                    creditId = "",
                    gender = 0,
                    id = 20,
                    knownForDepartment = "",
                    name = "Nicholson",
                    order = 1,
                    originalName = "",
                    popularity = 0.0,
                    profilePath = "Cuckoo.jpg"
                )
            ),
            crew = listOf(
                Crew(
                    adult = false,
                    creditId = "",
                    department = "",
                    gender = 0,
                    id = 123443321,
                    job = "Director",
                    knownForDepartment = "",
                    name = "Forest Gump",
                    originalName = "",
                    popularity = 0.0,
                    profilePath = "BoxOfChocolates.jpg"
                ),
                Crew(
                    adult = false,
                    creditId = "",
                    department = "",
                    gender = 0,
                    id = 123443321,
                    job = "Guy with an irrelevant job",
                    knownForDepartment = "",
                    name = "The one for the sound",
                    originalName = "Boomer",
                    popularity = 0.0,
                    profilePath = "BoxOfChocolates.jpg"
                )
            )
        )
    )

    private val reviewsResultsApi = ReviewResultsApi(
        author = "AuthorName",
        authorDetails = AuthorDetailsApi(
            avatarPath = "avatar.jpg",
            name = "testing",
            rating = 10,
            username = "testing"
        ),
        content = "Lorem ipsum test",
        createdAt = "2017-02-13T23:16:19.538Z",
        id = "",
        updatedAt = "",
        url = "",
    )

    private val reviewsResponseApi = ReviewsResponseApi(
        id = 1,
        page = 1,
        results = listOf(
            reviewsResultsApi,
            reviewsResultsApi,
            reviewsResultsApi,
        ),
        totalPages = 1,
        totalResults = 3,
    )

    private val expectedReviews: List<Review> = (1..3).map {
        Review(
            authorName = "AuthorName",
            rating = 10,
            content = "Lorem ipsum test",
            date = "13-02-2017",
        )
    }.toMutableList()

    private
    var movieRemote = FakeMovieRemote()

    private lateinit var repository: DetailsRepository

    @Before
    fun setUp() {
        repository = ProdDetailsRepository(
            movieRemote = movieRemote.mock,
        )
    }

    @Test
    fun testFetchMovieDetailsSuccessfully() = runTest {
        val request = DetailsRequestApi(movieId = 555)

        val expectedResult = movieDetails

        movieRemote.mockFetchMovieDetails(
            request = request,
            response = flowOf(detailsResponseApi)
        )

        val actualResult = repository.fetchMovieDetails(
            request = DetailsRequestApi(movieId = 555)
        ).first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }

    @Test
    fun testFetchMovieReviewsSuccessfully() = runTest {
        val request = ReviewsRequestApi(
            movieId = 555,
        )
        val expectedResult = expectedReviews

        movieRemote.mockFetchMovieReviews(
            request = request,
            response = flowOf(reviewsResponseApi)
        )

        val actualResult = repository.fetchMovieReviews(
            request = request
        ).first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }
}
