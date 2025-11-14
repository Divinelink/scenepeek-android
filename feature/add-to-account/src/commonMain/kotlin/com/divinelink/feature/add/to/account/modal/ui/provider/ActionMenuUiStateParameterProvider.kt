package com.divinelink.feature.add.to.account.modal.ui.provider

import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuUiState

@ExcludeFromKoverReport
class ActionMenuUiStateParameterProvider : PreviewParameterProvider<ActionMenuUiState> {
  override val values: Sequence<ActionMenuUiState> = sequenceOf(
    ActionMenuUiState.initial(
      media = MediaItemFactory.theWire(),
      entryPoint = ActionMenuEntryPoint.ListDetails(
        listName = "TV Shows",
        listId = 123,
      ),
    ),
    ActionMenuUiState.initial(
      media = MediaItemFactory.FightClub(),
      entryPoint = ActionMenuEntryPoint.Other,
    ),
  )
}
