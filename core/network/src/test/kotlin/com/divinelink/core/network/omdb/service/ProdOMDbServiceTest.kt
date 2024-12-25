package com.divinelink.core.network.omdb.service

import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.omdb.mapper.map
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.divinelink.core.testing.network.TestOMDbClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdOMDbServiceTest {

  private lateinit var service: ProdOMDbService
  private lateinit var testRestClient: TestOMDbClient

  @BeforeTest
  fun setUp() {
    testRestClient = TestOMDbClient()
  }

  @Test
  fun `test fetch imdb details with valid ratings`() = runTest {
    testRestClient.mockGetResponse<OMDbResponseApi>(
      url = "https://www.omdbapi.com/?i=tt0401729&apikey=1234567",
      jsonFileName = "omdb-details.json",
    )

    service = ProdOMDbService(testRestClient.restClient)

    val response = service.fetchImdbDetails("tt0401729").single()

    assertThat(response).isEqualTo(
      OMDbResponseApi(
        metascore = "51",
        imdbRating = "6.6",
        imdbVotes = "289,715",
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
          "BoxOffice": "${'$'}73,078,100",
          "Production": "N/A",
          "Website": "N/A",
          "Response": "True"
        }
      """.trimIndent(),
    )

    service = ProdOMDbService(testRestClient.restClient)

    val response = service.fetchImdbDetails("tt0401729").single()

    assertThat(response).isEqualTo(
      OMDbResponseApi(
        metascore = "51",
        imdbRating = "N/A",
        imdbVotes = "N/A",
      ),
    )

    assertThat(response.map()).isEqualTo(RatingDetails.Unavailable)
  }
}
