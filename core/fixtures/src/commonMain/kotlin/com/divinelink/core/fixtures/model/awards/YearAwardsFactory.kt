package com.divinelink.core.fixtures.model.awards

import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.awards.YearAwards
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType

object YearAwardsFactory {

  fun withMovies() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(
        media = MediaReference(
          mediaId = 550,
          mediaType = MediaType.MOVIE,
        ),
        winner = true,
        winningMedia = null,
      ),
      AwardNominee(
        media = MediaReference(
          mediaId = 551,
          mediaType = MediaType.MOVIE,
        ),
        winner = false,
        winningMedia = null,
      ),
    ),
  )

  fun withShows() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(
        media = MediaReference(
          mediaId = 1396,
          mediaType = MediaType.TV,
        ),
        winner = true,
        winningMedia = null,
      ),
      AwardNominee(
        media = MediaReference(
          mediaId = 1397,
          mediaType = MediaType.TV,
        ),
        winner = false,
        winningMedia = null,
      ),
    ),
  )

  fun withPersons() = YearAwards(
    year = "2000",
    nominees = listOf(
      AwardNominee(
        media = MediaReference(
          mediaId = 287,
          mediaType = MediaType.PERSON,
        ),
        winner = false,
        winningMedia = null,
      ),
      AwardNominee(
        media = MediaReference(
          mediaId = 288,
          mediaType = MediaType.PERSON,
        ),
        winner = true,
        winningMedia = MediaReference(
          mediaType = MediaType.MOVIE,
          mediaId = 550,
          title = "Fight Club",
        ),
      ),
    ),
  )

  fun empty() = YearAwards(
    year = "2000",
    nominees = emptyList(),
  )
}
