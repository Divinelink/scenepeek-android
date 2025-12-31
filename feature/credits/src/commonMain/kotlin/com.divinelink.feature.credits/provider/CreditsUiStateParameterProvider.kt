package com.divinelink.feature.credits.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.feature.credits.ui.CreditsTab
import com.divinelink.feature.credits.ui.CreditsUiContent
import com.divinelink.feature.credits.ui.CreditsUiState

@ExcludeFromKoverReport
class CreditsUiStateParameterProvider : PreviewParameterProvider<CreditsUiState> {
  override val values: Sequence<CreditsUiState> = sequenceOf(
    CreditsUiState(
      selectedTabIndex = 0,
      tabs = listOf(
        CreditsTab.Cast(2),
        CreditsTab.Crew(4),
      ),
      forms = mapOf(
        CreditsTab.Cast(2) to CreditsUiContent.Cast(
          cast = listOf(
            CreditsUiStateParameters.actor1,
            CreditsUiStateParameters.actor2,
            CreditsUiStateParameters.actor3,
          ),
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = listOf(
            SeriesCrewDepartment(
              department = "Department 1",
              crewList = listOf(
                CreditsUiStateParameters.crew1,
                CreditsUiStateParameters.crew2,
                CreditsUiStateParameters.crew3,
              ),
            ),
            SeriesCrewDepartment(
              department = "Department 2",
              crewList = listOf(
                CreditsUiStateParameters.crew1,
              ),
            ),
          ),
        ),
      ),
      obfuscateSpoilers = false,
    ),
    CreditsUiState(
      selectedTabIndex = 1,
      tabs = listOf(
        CreditsTab.Cast(2),
        CreditsTab.Crew(4),
      ),
      forms = mapOf(
        CreditsTab.Cast(2) to CreditsUiContent.Cast(
          cast = listOf(
            CreditsUiStateParameters.actor1,
            CreditsUiStateParameters.actor2,
            CreditsUiStateParameters.actor3,
          ),
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = listOf(
            SeriesCrewDepartment(
              department = "Department 1",
              crewList = listOf(
                CreditsUiStateParameters.crew1,
                CreditsUiStateParameters.crew2,
              ),
            ),
            SeriesCrewDepartment(
              department = "Department 2",
              crewList = listOf(
                CreditsUiStateParameters.crew3,
                CreditsUiStateParameters.crew4,
              ),
            ),
          ),
        ),
      ),
      obfuscateSpoilers = false,
    ),
    CreditsUiState(
      selectedTabIndex = 0,
      tabs = listOf(
        CreditsTab.Cast(0),
        CreditsTab.Crew(0),
      ),
      forms = mapOf(
        CreditsTab.Cast(0) to CreditsUiContent.Cast(
          cast = listOf(),
        ),
        CreditsTab.Crew(0) to CreditsUiContent.Crew(
          crew = listOf(),
        ),
      ),
      obfuscateSpoilers = false,
    ),
    CreditsUiState(
      selectedTabIndex = 1,
      tabs = listOf(
        CreditsTab.Cast(0),
        CreditsTab.Crew(0),
      ),
      forms = mapOf(
        CreditsTab.Cast(0) to CreditsUiContent.Cast(
          cast = listOf(),
        ),
        CreditsTab.Crew(0) to CreditsUiContent.Crew(
          crew = listOf(),
        ),
      ),
      obfuscateSpoilers = false,
    ),
    CreditsUiState(
      selectedTabIndex = 0,
      tabs = listOf(
        CreditsTab.Cast(2),
        CreditsTab.Crew(4),
      ),
      forms = mapOf(
        CreditsTab.Cast(2) to CreditsUiContent.Cast(
          cast = listOf(
            CreditsUiStateParameters.actor1,
            CreditsUiStateParameters.actor2,
            CreditsUiStateParameters.actor3,
          ),
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = listOf(
            SeriesCrewDepartment(
              department = "Department 1",
              crewList = listOf(
                CreditsUiStateParameters.crew1,
                CreditsUiStateParameters.crew2,
                CreditsUiStateParameters.crew3,
              ),
            ),
            SeriesCrewDepartment(
              department = "Department 2",
              crewList = listOf(
                CreditsUiStateParameters.crew1,
              ),
            ),
          ),
        ),
      ),
      obfuscateSpoilers = true,
    ),
  )
}

@ExcludeFromKoverReport
private object CreditsUiStateParameters {
  val actor1 = Person(
    id = 1,
    name = "Person 1",
    profilePath = "https://image.tmdb.org/t/p/w185/1.jpg",
    knownForDepartment = "Acting",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Character 1",
      ),
      PersonRole.SeriesActor(
        character = "Character 2",
      ),
    ),
  )

  val actor2 = Person(
    id = 2,
    name = "Person 2",
    profilePath = "https://image.tmdb.org/t/p/w185/2.jpg",
    knownForDepartment = "Acting",
    role = listOf(
      PersonRole.SeriesActor(
        character = "",
        totalEpisodes = 10,
      ),
    ),
  )

  val actor3 = Person(
    id = 3,
    name = "Person 3",
    profilePath = "https://image.tmdb.org/t/p/w185/1.jpg",
    knownForDepartment = "Acting",
    role = (1..5).map {
      PersonRole.SeriesActor(
        character = "Character $it",
        totalEpisodes = it,
      )
    },
  )

  val crew1 = Person(
    id = 1,
    name = "Crew 1",
    profilePath = "https://image.tmdb.org/t/p/w185/3.jpg",
    knownForDepartment = "Directing",
    role = listOf(
      PersonRole.Crew(
        job = "Job 1",
        creditId = "Credit 1",
        totalEpisodes = 2,
      ),
    ),
  )

  val crew2 = Person(
    id = 2,
    name = "Crew 2",
    profilePath = "https://image.tmdb.org/t/p/w185/4.jpg",
    knownForDepartment = "Directing",
    role = listOf(
      PersonRole.Crew(
        job = "Job 2",
        creditId = "Credit 2",
      ),
    ),
  )

  val crew3 = Person(
    id = 1,
    name = "Crew 3",
    profilePath = "https://image.tmdb.org/t/p/w185/5.jpg",
    knownForDepartment = "Directing",
    role = (4..8).map {
      PersonRole.Crew(
        job = "Job $it",
        creditId = "Credit $it",
        totalEpisodes = it.toLong(),
      )
    },
  )

  val crew4 = Person(
    id = 10,
    name = "Crew 2",
    profilePath = "https://image.tmdb.org/t/p/w185/4.jpg",
    knownForDepartment = "Directing",
    role = listOf(
      PersonRole.Crew(
        job = "",
        creditId = "Credit 10",
        totalEpisodes = 5,
      ),
    ),
  )
}
