package com.divinelink.core.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScenePeekMenuItem(
  intent: DropdownMenuIntent,
  onClick: () -> Unit,
) {
  DropdownMenuItem(
    modifier = Modifier.testTag(
      TestTags.Menu.item(intent.textRes.getString()),
    ),
    text = { Text(text = intent.textRes.getString()) },
    leadingIcon = {
      when (intent.icon) {
        is IconWrapper.Image -> Image(
          modifier = Modifier.size(MaterialTheme.dimensions.keyline_24),
          painter = painterResource((intent.icon as IconWrapper.Image).resourceId),
          colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
          contentDescription = intent.textRes.getString(),
        )
        is IconWrapper.Icon -> Icon(
          modifier = Modifier.size(MaterialTheme.dimensions.keyline_24),
          painter = painterResource((intent.icon as IconWrapper.Icon).resourceId),
          contentDescription = intent.textRes.getString(),
        )
        is IconWrapper.Vector -> Icon(
          modifier = Modifier.size(MaterialTheme.dimensions.keyline_24),
          imageVector = (intent.icon as IconWrapper.Vector).vector,
          contentDescription = intent.textRes.getString(),
        )
      }
    },
    onClick = onClick,
  )
}
