package com.divinelink.feature.credits.ui

import androidx.compose.runtime.Immutable

@Immutable
data class CreditsUiState(
  val selectedTabIndex: Int,
  val tabs: List<CreditsTab>,
  val forms: Map<CreditsTab, CreditsUiContent>,
  val obfuscateSpoilers: Boolean,
) {
  companion object {
    fun initial(): CreditsUiState = CreditsUiState(
      selectedTabIndex = 0,
      tabs = listOf(
        CreditsTab.Cast(0),
        CreditsTab.Crew(0),
      ),
      forms = mapOf(
        CreditsTab.Cast(0) to CreditsUiContent.Cast(emptyList()),
        CreditsTab.Crew(0) to CreditsUiContent.Crew(emptyList()),
      ),
      obfuscateSpoilers = false,
    )
  }
}
