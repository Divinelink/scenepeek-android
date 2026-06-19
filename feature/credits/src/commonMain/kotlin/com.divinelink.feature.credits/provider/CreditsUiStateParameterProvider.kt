package com.divinelink.feature.credits.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.credits.SeriesCastFactory
import com.divinelink.core.fixtures.details.credits.SeriesCrewListFactory
import com.divinelink.core.fixtures.details.person.PersonFactory
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
          cast = PersonFactory.officeCast,
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = listOf(
            SeriesCrewListFactory.art(),
            SeriesCrewListFactory.camera(),
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
          cast = SeriesCastFactory.cast(),
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = SeriesCrewListFactory.crewDepartments(),
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
        CreditsTab.Cast(8) to CreditsUiContent.Cast(
          cast = SeriesCastFactory.cast(),
        ),
        CreditsTab.Crew(4) to CreditsUiContent.Crew(
          crew = SeriesCrewListFactory.crewDepartments(),
        ),
      ),
      obfuscateSpoilers = true,
    ),
  )
}
