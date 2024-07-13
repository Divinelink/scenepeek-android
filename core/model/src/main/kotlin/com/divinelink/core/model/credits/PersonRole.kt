package com.divinelink.core.model.credits

sealed class PersonRole(val title: String?) {

  data class SeriesActor(
    val character: String?,
    val totalEpisodes: Int? = null,
  ) : PersonRole(character)

  data class MovieActor(val character: String?) : PersonRole(character)

  data class Crew(
    val job: String?,
    val creditId: String?,
    val totalEpisodes: Int? = null,
    val department: String? = null,
  ) : PersonRole(job)

  data object Director : PersonRole(null)

  data object Creator : PersonRole(null)
}
