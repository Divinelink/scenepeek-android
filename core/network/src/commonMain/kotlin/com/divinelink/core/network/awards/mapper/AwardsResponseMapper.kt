package com.divinelink.core.network.awards.mapper

import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.awards.YearAwards
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.awards.model.awards.AwardsResponse

fun AwardsResponse.map() = awards.map { award ->
  YearAwards(
    year = award.year,
    nominees = when {
      award.movies != null -> {
        award.movies.map { movie ->
          AwardNominee(
            mediaType = MediaType.MOVIE,
            id = movie.id,
            winner = movie.winner,
          )
        }
      }
      award.shows != null -> {
        award.shows.map { show ->
          AwardNominee(
            mediaType = MediaType.TV,
            id = show.id,
            winner = show.winner,
          )
        }
      }
      award.persons != null -> {
        award.persons.map { person ->
          AwardNominee(
            mediaType = MediaType.PERSON,
            id = person.id,
            winner = person.winner,
          )
        }
      }
      else -> emptyList()
    },
  )
}
