package com.divinelink.core.data.details.mapper

import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person

fun AggregateCreditsEntity.map() = AggregateCredits(
  cast = cast,
  crewDepartments = crew.map(),
  id = id,
)

/**
 * Maps every crew department to its persons.
 */
fun List<Person>.map(): List<SeriesCrewDepartment> = this
  .flatMap { person ->
    person.role
      .filterIsInstance<PersonRole.Crew>()
      .map { crewRole -> person to crewRole }
  }
  .groupBy(
    keySelector = { (_, crewRole) -> crewRole.department },
    valueTransform = { (person, _) -> person },
  )
  .mapNotNull { (department, people) ->
    if (department == null) {
      null
    } else {
      SeriesCrewDepartment(department, people)
    }
  }
