package com.divinelink.core.fixtures.details.credits

import com.divinelink.core.fixtures.details.person.PersonFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person

object SeriesCrewListFactory {

  fun crewDepartments() = listOf(
    art(),
    camera(),
    costumeAndMakeUp(),
  )

  fun art() = SeriesCrewDepartment(
    department = "Art",
    crewList = listOf(
      PersonFactory.Art.steveRostine(),
      PersonFactory.Art.philipDShea(),
      PersonFactory.Art.donaldLeeHarris(),
      PersonFactory.Art.melodyMelton(),
    ),
  )

  fun unsortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.ronNichols(),
      PersonFactory.Camera.peterSmokler(),
    ),
  )

  fun camera() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.ronNichols(),
      PersonFactory.Camera.peterSmokler(),
    ),
  )

  fun sortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.peterSmokler(),
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.ronNichols(),
    ),
  )

  fun costumeAndMakeUp() = SeriesCrewDepartment(
    department = "Costume & Make-Up",
    crewList = listOf(
      Person(
        id = 1325655,
        name = "Elinor Bardach",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Costume Supervisor",
            creditId = "5bdaa36e92514153fb008795",
            totalEpisodes = 4,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 2166015,
        name = "Carey Bennett",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Costume Designer",
            creditId = "5bdaa3650e0a2603bf008174",
            totalEpisodes = 4,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 1664353,
        name = "Cyndra Dunn",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Key Hair Stylist",
            creditId = "5bdaa74d0e0a2603b4008cbf",
            totalEpisodes = 3,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 1543004,
        name = "Maria Valdivia",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Key Hair Stylist",
            creditId = "5bdaa38cc3a3680772009017",
            totalEpisodes = 1,
            department = "Costume & Make-Up",
          ),
        ),
      ),
    ),
  )
}
