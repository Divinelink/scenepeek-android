package com.divinelink.feature.awards.popular.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.awards.CeremonyFactory
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.awards.popular.AwardsUiState

class AwardsUiStateParameterProvider : PreviewParameterProvider<AwardsUiState> {
  override val values: Sequence<AwardsUiState> = sequenceOf(
    AwardsUiState.initial,
    AwardsUiState.initial.copy(
      loading = false,
      error = BlankSlateState.Offline,
    ),
    AwardsUiState.initial.copy(
      loading = false,
      ceremonies = CeremonyFactory.all(),
    ),
  )
}
