package com.divinelink.feature.awards.popular

import com.divinelink.core.model.awards.Ceremony

sealed interface AwardsAction {
  data class OnCeremonyClick(val ceremony: Ceremony) : AwardsAction
  data object OnRetry : AwardsAction
}
