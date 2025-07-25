package com.divinelink.feature.lists.create.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.create.CreateListUiState

class CreateListUiStateParameterProvider : PreviewParameterProvider<CreateListUiState> {
  override val values: Sequence<CreateListUiState> = sequenceOf(
    CreateListUiState.initial,
    CreateListUiState(
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = true,
      editMode = false,
      snackbarMessage = null,
    ),
    CreateListUiState(
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = false,
      editMode = true,
      snackbarMessage = null,
    ),
    CreateListUiState(
      name = "My Favorite Movies",
      description = "A collection of my all-time favorite movies.",
      public = false,
      editMode = false,
      snackbarMessage = SnackbarMessage.from(
        text = UIText.ResourceText(
          R.string.feature_lists_create_successfully,
          "My Favorite Movies",
        ),
      ),
    ),
  )
}
