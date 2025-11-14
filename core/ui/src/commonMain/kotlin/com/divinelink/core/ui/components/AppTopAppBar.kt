package com.divinelink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_navigate_up_button_content_description
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
  modifier: Modifier = Modifier,
  text: UIText,
  scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
  topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  ),
  contentColor: Color = MaterialTheme.colorScheme.onSurface,
  actions: @Composable RowScope.() -> Unit = {},
  progress: Float = 1f,
  onNavigateUp: () -> Unit,
) {
  val (alpha, offsetY) = remember(progress) {
    if (progress > 0.5f) {
      val calculatedAlpha = ((progress - 0.5f) / 0.3f).coerceIn(0f, 1f)
      val calculatedOffsetY = (1f - calculatedAlpha) * -32f
      calculatedAlpha to calculatedOffsetY
    } else {
      0f to 32f
    }
  }

  TopAppBar(
    modifier = modifier
      .background(animateColorFromProgress(alpha))
      .testTag(TestTags.Components.TopAppBar.TOP_APP_BAR),
    scrollBehavior = scrollBehavior,
    colors = topAppBarColors,
    title = {
      Text(
        modifier = Modifier
          .testTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE)
          .offset(y = offsetY.dp)
          .alpha(alpha),
        text = text.getString(),
        color = contentColor,
        maxLines = 2,
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      IconButton(
        modifier = Modifier.testTag(TestTags.Components.TopAppBar.NAVIGATE_UP),
        onClick = onNavigateUp,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
          contentDescription = stringResource(
            UiString.core_ui_navigate_up_button_content_description,
          ),
          tint = contentColor,
        )
      }
    },
    actions = actions,
  )
}

@Composable
fun animateColorFromProgress(toolbarProgress: Float): Color = when {
  toolbarProgress < 0.3f -> Color.Transparent
  else -> MaterialTheme.colorScheme.surface.copy(
    alpha = ((toolbarProgress - 0.3f) / 0.7f).coerceIn(0f, 1f),
  )
}
