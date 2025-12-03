package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_back_to_top
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScrollToTopButton(
  modifier: Modifier = Modifier,
  visible: Boolean,
  onClick: () -> Unit,
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(tween(easing = EaseIn)),
    exit = fadeOut(tween(easing = EaseOut)),
    modifier = modifier
      .padding(bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_16),
    label = "Show scroll to top button animation",
  ) {
    Button(
      modifier = Modifier
        .testTag(TestTags.SCROLL_TO_TOP_BUTTON)
        .height(MaterialTheme.dimensions.keyline_32)
        .clip(shape = CircleShape),
      contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_16),
      colors = ButtonDefaults.buttonColors().copy(
        containerColor = MaterialTheme.colorScheme.onSurface,
        contentColor = MaterialTheme.colorScheme.surface,
      ),
      onClick = onClick,
    ) {
      Text(
        text = stringResource(UiString.core_ui_back_to_top),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.W600,
        fontSize = 11.5.sp,
      )
    }
  }
}

@Composable
@Previews
fun ScrollToTopButtonPreview() {
  AppTheme {
    Surface {
      ScrollToTopButton(visible = true) {}
    }
  }
}
