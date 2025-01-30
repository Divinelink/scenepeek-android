package com.divinelink.core.fixtures.model.person.credit

import com.divinelink.core.model.details.person.GroupedPersonCredits

object GroupedPersonCreditsSample {

  fun movies(): GroupedPersonCredits = mapOf(
    "Acting" to listOf(
      PersonCastCreditFactory.bruceAlmighty(),
      PersonCastCreditFactory.littleMissSunshine(),
      PersonCastCreditFactory.despicableMe(),
    ),
    "Writing" to listOf(PersonCrewCreditFactory.the40YearOldVirgin()),
    "Production" to listOf(
      PersonCrewCreditFactory.getSmart(),
      PersonCrewCreditFactory.theIncredibleBurtWonderstone(),
    ),
  )

  fun tvShows(): GroupedPersonCredits = mapOf(
    "Acting" to listOf(PersonCastCreditFactory.theOffice()),
    "Production" to listOf(PersonCrewCreditFactory.riot()),
  )
}
