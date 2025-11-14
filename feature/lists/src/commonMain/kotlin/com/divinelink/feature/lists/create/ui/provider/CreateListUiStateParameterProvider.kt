package com.divinelink.feature.lists.create.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.lists.Res
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.feature_lists_create_successfully
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class CreateListUiStateParameterProvider : PreviewParameterProvider<CreateListUiState> {
  override val values: Sequence<CreateListUiState> = sequenceOf(
    CreateListUiState.initial,
    CreateListUiState(
      id = 1,
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = true,
      editMode = false,
      backdrop = null,
      snackbarMessage = null,
      loading = false,
    ),
    CreateListUiState(
      id = 2,
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = false,
      editMode = true,
      backdrop = "/ew5FcYiRhTYNJAkxoVPMNlCOdVn.jpg",
      snackbarMessage = null,
      loading = false,
    ),
    CreateListUiState(
      id = 3,
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = false,
      editMode = false,
      backdrop = "",
      snackbarMessage = SnackbarMessage.from(
        text = UIText.ResourceText(
          Res.string.feature_lists_create_successfully,
          "My Favorite Movies",
        ),
      ),
      loading = false,
    ),
    CreateListUiState(
      id = 3,
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = false,
      editMode = false,
      backdrop = "",
      snackbarMessage = null,
      loading = true,
    ),
  )
}
