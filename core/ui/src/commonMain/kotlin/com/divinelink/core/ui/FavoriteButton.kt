package com.divinelink.core.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.resources.core_ui_add_to_collection_button_content_description
import com.divinelink.core.ui.resources.core_ui_added_to_your_saved
import com.divinelink.core.ui.resources.core_ui_removed_from_your_saved
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoriteButton(
  modifier: Modifier = Modifier,
  isFavorite: Boolean,
  inactiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  onClick: () -> Unit,
) {
  val color by animateColorAsState(
    targetValue = when (isFavorite) {
      true -> MaterialTheme.colors.redHighlight
      false -> inactiveColor
    },
    label = "Like button color",
  )
  var snackbarMessage by remember { mutableStateOf<SnackbarMessage?>(null) }
  val scope = rememberCoroutineScope()

  fun onToggleSave() {
    scope.launch { onClick() }

    val text = if (!isFavorite) {
      UiString.core_ui_added_to_your_saved
    } else {
      UiString.core_ui_removed_from_your_saved
    }
    snackbarMessage = SnackbarMessage.from(text = UIText.ResourceText(text))
  }

  SnackbarMessageHandler(
    snackbarMessage = snackbarMessage,
    onDismissSnackbar = { snackbarMessage = null },
    onShowMessage = {},
  )

  Box(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_4)
      .clip(MaterialTheme.shape.rounded)
      .clickable { onToggleSave() }
      .size(MaterialTheme.dimensions.keyline_40),
  ) {
    Crossfade(
      modifier = Modifier.align(Alignment.Center),
      targetState = isFavorite,
      label = "Like button",
    ) { favorite ->
      val image = when (favorite) {
        true -> Icons.Rounded.Save
        false -> Icons.Outlined.Save
      }
      Icon(
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_26),
        imageVector = image,
        tint = color,
        contentDescription = stringResource(
          UiString.core_ui_add_to_collection_button_content_description,
        ),
      )
    }
  }
}
