package com.divinelink.core.fixtures.model.person.credit

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.person.credits.PersonCredit

object PersonCrewCreditFactory {

  fun the40YearOldVirgin() = PersonCredit(
    creditId = "52fe446ac3a36847f8094c49",
    media = MediaItemFactory.the40YearOldVirgin(),
    role = PersonRole.Crew(
      creditId = "52fe446ac3a36847f8094c49",
      department = "Writing",
      job = "Screenplay",
    ),
  )

  fun getSmart() = PersonCredit(
    creditId = "52fe44749251416c750355d3",
    media = MediaItemFactory.getSmart(),
    role = PersonRole.Crew(
      creditId = "52fe44749251416c750355d3",
      department = "Production",
      job = "Executive Producer",
    ),
  )

  fun theIncredibleBurtWonderstone() = PersonCredit(
    creditId = "5640b22e925141705c00145f",
    media = MediaItemFactory.theIncredibleBurtWonderstone(),
    role = PersonRole.Crew(
      creditId = "5640b22e925141705c00145f",
      department = "Production",
      job = "Producer",
    ),
  )

  fun riot() = PersonCredit(
    creditId = "53762236c3a3681ed4001579",
    media = MediaItemFactory.riot(),
    role = PersonRole.Crew(
      creditId = "53762236c3a3681ed4001579",
      job = "Executive Producer",
      department = "Production",
      totalEpisodes = 4,
    ),
  )

  fun all() = listOf(
    the40YearOldVirgin(),
    getSmart(),
    theIncredibleBurtWonderstone(),
    riot(),
  )

  fun sortedByDate() = listOf(
    riot(),
    theIncredibleBurtWonderstone(),
    getSmart(),
    the40YearOldVirgin(),
  )

  fun productionSortedByPopularity() = listOf(
    riot(),
    getSmart(),
    theIncredibleBurtWonderstone(),
  )
}
