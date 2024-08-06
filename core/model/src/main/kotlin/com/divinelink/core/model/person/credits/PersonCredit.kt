package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaType

sealed class PersonCredit(
  open val id: Long,
  open val mediaName: String,
  open val mediaOriginalName: String,
  open val mediaReleaseDate: String,
  open val mediaType: MediaType,
  open val posterPath: String?,
  open val role: PersonRole,
) {
  abstract val adult: Boolean
  abstract val backdropPath: String?
  abstract val genreIds: List<Int>
  abstract val originalLanguage: String
  abstract val overview: String
  abstract val popularity: Double
  abstract val voteAverage: Double
  abstract val voteCount: Long
  abstract val creditId: String
}
