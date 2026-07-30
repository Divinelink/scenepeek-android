package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.details.credits.CrewApi
import com.divinelink.core.network.media.model.details.credits.CrewJob

fun List<CrewApi>.map() = this
  .groupBy { it.id }
  .map { (_, crewEntries) ->
    val firstEntry = crewEntries.first()

    val roles = crewEntries
      .mapNotNull { it.job.jobToPersonRole() }
      .distinct()

    MediaItem.Person(
      id = firstEntry.id,
      name = firstEntry.name,
      profilePath = firstEntry.profilePath,
      knownForDepartment = firstEntry.knownForDepartment,
      gender = Gender.from(firstEntry.gender),
      role = roles,
    )
  }
  .filter { it.role.isNotEmpty() }
  .sortedWith(
    compareBy {
      when (it.role.first()) {
        is PersonRole.Director -> 1
        is PersonRole.Screenplay -> 2
        is PersonRole.Novel -> 3
        is PersonRole.Author -> 4
        is PersonRole.Writer -> 5
        is PersonRole.Composer -> 6
        is PersonRole.Story -> 7
        else -> 8
      }
    },
  )

fun String.jobToPersonRole(): PersonRole? = when (this) {
  CrewJob.AUTHOR.value -> PersonRole.Author
  CrewJob.DIRECTOR.value -> PersonRole.Director
  CrewJob.SCREENPLAY.value -> PersonRole.Screenplay
  CrewJob.NOVEL.value -> PersonRole.Novel
  CrewJob.COMPOSER.value -> PersonRole.Composer
  CrewJob.WRITER.value -> PersonRole.Writer
  CrewJob.STORY.value -> PersonRole.Story
  else -> null
}
