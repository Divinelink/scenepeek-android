package com.divinelink.feature.awards.popular

sealed interface AwardsAction {
  data class OnCeremonyClick(val id: String) : AwardsAction
  data object OnRetry : AwardsAction
}
