package com.divinelink.core.network.omdb.service

import com.divinelink.core.commons.provider.SecretProvider
import com.divinelink.core.model.details.rating.ExternalRatings
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.omdb.mapper.map
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.divinelink.core.testing.commons.provider.TestSecretProvider
import com.divinelink.core.testing.network.TestOMDbClient
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdOMDbServiceTest {

  private lateinit var service: ProdOMDbService
  private lateinit var testRestClient: TestOMDbClient
  private lateinit var secrets: SecretProvider

  @BeforeTest
  fun setUp() {
    testRestClient = TestOMDbClient()
    secrets = TestSecretProvider()
  }

  @Test
  fun `test fetch imdb details with valid ratings`() = runTest {
    testRestClient.mockGetResponse<OMDbResponseApi>(
      url = "https://www.omdbapi.com/?i=tt0401729&apikey=1234567",
      jsonFileName = "omdb-details.json",
    )

    service = ProdOMDbService(
      client = testRestClient.restClient,
      secrets = secrets,
    )

    val response = service.fetchExternalRatings("tt0401729").single()

    response shouldBe OMDbResponseApi(
      metascore = "51",
      imdbRating = "6.6",
      imdbVotes = "289,715",
      ratings = listOf(
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Internet Movie Database",
          value = "6.6/10",
        ),
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Rotten Tomatoes",
          value = "52%",
        ),
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Metacritic",
          value = "51/100",
        ),
      ),
    )
  }

  @Test
  fun `test fetch imdb details with invalid ratings`() = runTest {
    testRestClient.mockGetResponseJson<OMDbResponseApi>(
      url = "https://www.omdbapi.com/?i=tt0401729&apikey=1234567",
      json = """
        {
          "Title": "John Carter",
          "Year": "2012",
          "Rated": "PG-13",
          "Released": "09 Mar 2012",
          "Runtime": "132 min",
          "Genre": "Action, Adventure, Sci-Fi",
          "Director": "Andrew Stanton",
          "Writer": "Andrew Stanton, Mark Andrews, Michael Chabon",
          "Actors": "Taylor Kitsch, Lynn Collins, Willem Dafoe",
          "Plot": "A war-weary former army captain is inexplicably transported to Mars and reluctantly becomes embroiled in a conflict of epic proportions.",
          "Language": "English",
          "Country": "United States",
          "Awards": "2 wins & 8 nominations",
          "Poster": "https://m.media-amazon.com/images/M/MV5BZWNmZGYzZjUtODRmOS00ODgzLWE4NWQtMDI3MGUwNjRjYjY0XkEyXkFqcGc@._V1_SX300.jpg",
          "Ratings": [
            {
              "Source": "Internet Movie Database",
              "Value": "6.6/10"
            },
            {
              "Source": "Rotten Tomatoes",
              "Value": "52%"
            },
            {
              "Source": "Metacritic",
              "Value": "51/100"
            }
          ],
          "Metascore": "51",
          "imdbRating": "N/A",
          "imdbVotes": "N/A",
          "imdbID": "tt0401729",
          "Type": "movie",
          "DVD": "N/A",
          "BoxOffice": "$73,078,100",
          "Production": "N/A",
          "Website": "N/A",
          "Response": "True"
        }
      """.trimIndent(),
    )

    service = ProdOMDbService(
      client = testRestClient.restClient,
      secrets = secrets,
    )

    val response = service.fetchExternalRatings("tt0401729").single()

    response shouldBe OMDbResponseApi(
      metascore = "51",
      imdbRating = "N/A",
      imdbVotes = "N/A",
      ratings = listOf(
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Internet Movie Database",
          value = "6.6/10",
        ),
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Rotten Tomatoes",
          value = "52%",
        ),
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Metacritic",
          value = "51/100",
        ),
      ),
    )

    response.map() shouldBe ExternalRatings(
      imdb = RatingDetails.Unavailable,
      rt = RatingDetails.Rating(52),
      metascore = RatingDetails.Rating(51),
    )
  }
}
