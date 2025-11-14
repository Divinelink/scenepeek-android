package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.clear_filters_button_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ClearButton(
  modifier: Modifier = Modifier,
  onClearClick: () -> Unit,
) {
  val containerColor = MaterialTheme.colorScheme.surfaceVariant
  OutlinedButton(
    onClick = onClearClick,
    modifier = modifier
      .testTag(TestTags.Components.Button.CLEAR_FILTERS)
      .size(MaterialTheme.dimensions.keyline_40),
    border = null,
    shape = CircleShape,
    contentPadding = PaddingValues(0.dp),
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = containerColor,
      contentColor = contentColorFor(containerColor),
    ),
  ) {
    Icon(
      imageVector = Icons.Default.Clear,
      contentDescription = stringResource(UiString.clear_filters_button_content_description),
    )
  }
}

fun LazyListScope.clearButton(
  isVisible: Boolean,
  onClearClick: () -> Unit,
) {
  item {
    AnimatedVisibility(
      enter = slideInHorizontally() + expandHorizontally(),
      exit = slideOutHorizontally() + shrinkHorizontally(),
      visible = isVisible,
    ) {
      ClearButton(
        onClearClick = onClearClick,
      )
    }
  }
}
