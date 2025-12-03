package com.divinelink.core.fixtures.model.person.credit

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.person.credits.PersonCredit

object PersonCastCreditFactory {

  fun bruceAlmighty() = PersonCredit(
    creditId = "52fe4236c3a36847f800c65f",
    media = MediaItemFactory.bruceAlmighty(),
    role = PersonRole.MovieActor(
      character = "Evan Baxter",
      order = 6,
    ),
  )

  fun littleMissSunshine() = PersonCredit(
    creditId = "52fe4274c3a36847f80200d3",
    role = PersonRole.MovieActor(
      character = "Frank Ginsberg",
      order = 2,
    ),
    media = MediaItemFactory.littleMissSunshine(),
  )

  fun despicableMe() = PersonCredit(
    creditId = "52fe43e2c3a368484e003e77",
    media = MediaItemFactory.despicableMe(),
    role = PersonRole.MovieActor(
      character = "Gru (voice)",
      order = 0,
    ),
  )

  fun theOffice() = PersonCredit(
    creditId = "525730a9760ee3776a344705",
    media = MediaItemFactory.theOffice(),
    role = PersonRole.SeriesActor(
      character = "Michael Scott",
      creditId = "525730a9760ee3776a344705",
      totalEpisodes = 140,
    ),
  )

  fun all() = listOf(
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice(),
  )

  fun sortedByDate() = listOf(
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty(),
  )

  fun knownFor() = listOf(
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe(),
    com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty(),
  )

  class PersonCastCreditFactoryWizard(private var personCredit: PersonCredit) {

    fun withSeriesCharacter(character: String) = apply {
      personCredit = personCredit.copy(
        role = (this.personCredit.role as PersonRole.SeriesActor).copy(
          character = character,
        ),
      )
    }

    fun withJob(job: String) = apply {
      personCredit = personCredit.copy(
        role = (this.personCredit.role as PersonRole.Crew).copy(
          job = job,
        ),
      )
    }

    fun build(): PersonCredit = personCredit
  }

  fun PersonCredit.toWizard(block: PersonCastCreditFactoryWizard.() -> Unit) =
    PersonCastCreditFactoryWizard(this).apply(block).build()
}
