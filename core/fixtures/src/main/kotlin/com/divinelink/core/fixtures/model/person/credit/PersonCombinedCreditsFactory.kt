package com.divinelink.core.fixtures.model.person.credit

import com.divinelink.core.model.person.credits.PersonCombinedCredits

object PersonCombinedCreditsFactory {

  fun all() = PersonCombinedCredits(
    id = 4495,
    cast = PersonCastCreditFactory.all(),
    crew = PersonCrewCreditFactory.all(),
  )

  fun sortedByDate() = PersonCombinedCredits(
    id = 4495,
    cast = PersonCastCreditFactory.sortedByDate(),
    crew = PersonCrewCreditFactory.sortedByDate(),
  )
}
