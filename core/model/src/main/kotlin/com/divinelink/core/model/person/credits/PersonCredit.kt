package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem

sealed class PersonCredit(
  open val id: Long,
  open val mediaItem: MediaItem.Media,
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
