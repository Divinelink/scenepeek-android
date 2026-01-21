package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.details.person.model.PersonCreditsApi

fun PersonCreditsApi.toEntityCast() = cast
  .filterNot { it.adult }
  .filter { MediaType.isMedia(it.mediaType) }
  .map {
    PersonCastCreditEntity(
      id = it.id.toLong(),
      personId = this.id,
      releaseDate = it.releaseDate,
      firstAirDate = it.firstAirDate,
      character = it.character,
      creditId = it.creditId,
      episodeCount = it.episodeCount?.toLong(),
      mediaType = it.mediaType,
      creditOrder = it.order?.toLong(),
    )
  }

fun PersonCreditsApi.toEntityCrew(): List<PersonCrewCreditEntity> = crew
  .filterNot { it.adult }
  .filter { MediaType.isMedia(it.mediaType) }
  .map {
    PersonCrewCreditEntity(
      id = it.id.toLong(),
      personId = this.id,
      releaseDate = it.releaseDate,
      firstAirDate = it.firstAirDate,
      job = it.job,
      creditId = it.creditId,
      mediaType = it.mediaType,
      department = it.department,
      episodeCount = it.episodeCount?.toLong(),
    )
  }
