package com.divinelink.core.testing.factories.api.awards

import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.awards.model.awards.AwardNomineeResponse
import com.divinelink.core.network.awards.model.awards.AwardsResponse
import com.divinelink.core.network.awards.model.awards.AwardsYearResponse

object AwardsResponseFactory {

  fun nomineeResponse(
    id: Long = 550,
    winner: Boolean = false,
    countries: List<String>? = null,
    winningMedia: MediaReference? = null,
  ) = AwardNomineeResponse(
    id = id,
    winner = winner,
    mediaId = winningMedia?.mediaId,
    type = winningMedia?.mediaType?.value,
    title = winningMedia?.title,
    countries = countries,
  )

  fun withMovies() = AwardsResponse(
    awards = listOf(
      AwardsYearResponse(
        year = "2000",
        movies = listOf(
          nomineeResponse(id = 550, winner = true, countries = listOf("PS", "LI")),
          nomineeResponse(id = 551, winner = false),
        ),
        shows = null,
        persons = null,
      ),
    ),
  )

  fun withShows() = AwardsResponse(
    awards = listOf(
      AwardsYearResponse(
        year = "2000",
        movies = null,
        shows = listOf(
          nomineeResponse(id = 1396, winner = true),
          nomineeResponse(id = 1397, winner = false),
        ),
        persons = null,
      ),
    ),
  )

  fun withPersons() = AwardsResponse(
    awards = listOf(
      AwardsYearResponse(
        year = "2000",
        movies = null,
        shows = null,
        persons = listOf(
          nomineeResponse(id = 287, winner = false),
          nomineeResponse(
            id = 288,
            winner = true,
            winningMedia = MediaReference(
              mediaType = MediaType.MOVIE,
              mediaId = 550,
              title = "Fight Club",
            ),
          ),
        ),
      ),
    ),
  )

  fun empty() = AwardsResponse(
    awards = listOf(
      AwardsYearResponse(
        year = "2000",
        movies = null,
        shows = null,
        persons = null,
      ),
    ),
  )
}
