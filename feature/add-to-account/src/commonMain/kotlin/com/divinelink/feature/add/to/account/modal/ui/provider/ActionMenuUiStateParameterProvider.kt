package com.divinelink.feature.add.to.account.modal.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuUiState

@ExcludeFromKoverReport
class ActionMenuUiStateParameterProvider : PreviewParameterProvider<ActionMenuUiState> {
  override val values: Sequence<ActionMenuUiState> = sequenceOf(
    ActionMenuUiState.initial(
      entryPoint = ActionMenuEntryPoint.ListDetails(
        media = MediaItemFactory.theWire(),
        listName = "TV Shows",
        listId = 123,
      ),
    ),
    ActionMenuUiState.initial(
      entryPoint = ActionMenuEntryPoint.Other(
        media = MediaItemFactory.FightClub(),
      ),
    ),
    ActionMenuUiState.initial(
      entryPoint = ActionMenuEntryPoint.ShowProgress(
        showId = MediaItemFactory.theOffice().id,
      ),
    ),
  )
}
