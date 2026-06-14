package com.divinelink.core.testing.factories.api.awards

import com.divinelink.core.network.awards.model.awards.AwardNomineeResponse
import com.divinelink.core.network.awards.model.awards.AwardsResponse
import com.divinelink.core.network.awards.model.awards.AwardsYearResponse

object AwardsResponseFactory {

  fun nomineeResponse(
    id: Int = 550,
    winner: Boolean = false,
  ) = AwardNomineeResponse(
    id = id,
    winner = winner,
  )

  fun withMovies() = AwardsResponse(
    awards = listOf(
      AwardsYearResponse(
        year = "2000",
        movies = listOf(
          nomineeResponse(id = 550, winner = true),
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
          nomineeResponse(id = 288, winner = true),
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
