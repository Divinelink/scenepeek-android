package com.divinelink.core.network.awards.mapper

import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.awards.YearAwards
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.awards.model.awards.AwardsResponse

fun AwardsResponse.map() = awards.map { award ->
  YearAwards(
    year = award.year,
    nominees = when {
      award.movies != null -> award.movies.map { movie ->
        AwardNominee(
          media = MediaReference(
            mediaType = MediaType.MOVIE,
            mediaId = movie.id,
          ),
          winner = movie.winner,
          winningMedia = null,
        )
      }
      award.shows != null -> award.shows.map { show ->
        AwardNominee(
          media = MediaReference(
            mediaType = MediaType.TV,
            mediaId = show.id,
          ),
          winner = show.winner,
          winningMedia = null,
        )
      }
      award.persons != null -> award.persons.map { person ->
        AwardNominee(
          media = MediaReference(
            mediaType = MediaType.PERSON,
            mediaId = person.id,
          ),
          winner = person.winner,
          winningMedia = if (person.mediaId != null) {
            MediaReference(
              mediaId = person.mediaId,
              mediaType = MediaType.from(person.type),
              title = person.title,
            )
          } else {
            null
          },
        )
      }
      else -> emptyList()
    },
  )
}
