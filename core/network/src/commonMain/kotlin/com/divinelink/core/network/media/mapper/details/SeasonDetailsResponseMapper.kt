package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.details.season.EpisodeResponse
import com.divinelink.core.network.media.model.details.season.SeasonDetailsResponse
import com.divinelink.core.network.media.model.details.toHourMinuteFormat
import com.divinelink.core.network.media.model.states.EpisodeAccountStatesResponse
import com.divinelink.core.network.media.model.states.RateResponseApi

fun SeasonDetailsResponse.map(): SeasonDetails = SeasonDetails(
  id = id,
  name = name,
  overview = overview,
  airDate = airDate,
  episodeCount = episodes.count(),
  posterPath = posterPath,
  voteAverage = voteAverage,
  totalRuntime = episodes
    .filter { it.runtime != null }
    .sumOf { it.runtime!! }
    .toHourMinuteFormat(),
  episodes = episodes.map { episode ->
    episode.map(accountRating = ratings.map(episode.id))
  },
  guestStars = aggregateGuestStars(episodes),
)

private fun EpisodeAccountStatesResponse?.map(episodeId: Int): Int? {
  val episodeRating = this
    ?.results
    ?.find { ratingResponse -> ratingResponse.id == episodeId }

  return when (episodeRating?.rated) {
    RateResponseApi.False -> null
    is RateResponseApi.Value -> episodeRating.rated.value.toInt()
    null -> null
  }
}

private fun aggregateGuestStars(allEpisodes: List<EpisodeResponse>): List<Person> = allEpisodes
  .flatMap { it.guestStars }
  .groupBy { it.id }
  .map { (id, cast) ->
    val firstCast = cast.first()

    val roles = cast
      .groupingBy { it }
      .eachCount()

    Person(
      id = id,
      name = firstCast.name,
      profilePath = firstCast.profilePath,
      gender = Gender.from(firstCast.gender),
      knownForDepartment = firstCast.knownForDepartment,
      role = roles.map { (cast, count) ->
        PersonRole.SeriesActor(
          character = cast.character,
          creditId = cast.creditId,
          totalEpisodes = count,
          order = cast.order,
        )
      },
    )
  }
  .sortedByDescending {
    it.role.sumOf { role -> (role as? PersonRole.SeriesActor)?.totalEpisodes ?: 0 }
  }
  .distinctBy { it.id }
