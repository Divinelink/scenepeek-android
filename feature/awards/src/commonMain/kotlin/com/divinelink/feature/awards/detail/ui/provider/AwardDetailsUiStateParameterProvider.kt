package com.divinelink.feature.awards.detail.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.awards.CeremonyFactory
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.awards.detail.AwardDetailsUiState

class AwardDetailsUiStateParameterProvider : PreviewParameterProvider<AwardDetailsUiState> {
  override val values: Sequence<AwardDetailsUiState> = sequenceOf(
    AwardDetailsUiState.initial(ceremony = CeremonyFactory.oscars()).copy(
      loading = false,
    ),
    AwardDetailsUiState.initial(ceremony = CeremonyFactory.oscars()).copy(
      loading = true,
    ),
    AwardDetailsUiState.initial(ceremony = CeremonyFactory.oscars()).copy(
      loading = false,
      error = BlankSlateState.Generic,
    ),
  )
}
