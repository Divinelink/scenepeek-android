package com.divinelink.core.model.credits

import kotlinx.serialization.Serializable

@Serializable
sealed class PersonRole(val title: String?) {

  @Serializable
  data class SeriesActor(
    val character: String,
    val creditId: String,
    val totalEpisodes: Int? = null,
    val order: Int? = null,
  ) : PersonRole(character)

  @Serializable
  data class MovieActor(
    val character: String?,
    val order: Int? = Int.MAX_VALUE,
  ) : PersonRole(character)

  @Serializable
  data class Crew(
    val job: String?,
    val creditId: String?,
    val totalEpisodes: Long? = null,
    val department: String? = null,
  ) : PersonRole(job)

  data object Director : PersonRole(null)
  data object Screenplay : PersonRole(null)
  data object Writer : PersonRole(null)
  data object Novel : PersonRole(null)
  data object Author : PersonRole(null)
  data object Composer : PersonRole(null)
  data object Story : PersonRole(null)

  data object Creator : PersonRole(null)

  data object Unknown : PersonRole(null)
}
