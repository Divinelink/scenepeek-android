package com.divinelink.core.fixtures.model.awards

import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.awards.YearAwards
import com.divinelink.core.model.media.MediaType

object YearAwardsFactory {

  fun withMovies() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(id = 550, winner = true, mediaType = MediaType.MOVIE),
      AwardNominee(id = 551, winner = false, mediaType = MediaType.MOVIE),
    ),
  )

  fun withShows() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(id = 1396, winner = true, mediaType = MediaType.TV),
      AwardNominee(id = 1397, winner = false, mediaType = MediaType.TV),
    ),
  )

  fun withPersons() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(id = 287, winner = false, mediaType = MediaType.PERSON),
      AwardNominee(id = 288, winner = true, mediaType = MediaType.PERSON),
    ),
  )

  fun empty() = YearAwards(
    year = "2000",
    nominees = emptyList(),
  )
}
