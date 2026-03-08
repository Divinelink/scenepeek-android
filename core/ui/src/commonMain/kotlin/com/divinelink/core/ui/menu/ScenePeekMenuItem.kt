package com.divinelink.core.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScenePeekMenuItem(
  intent: DropdownMenuIntent,
  onClick: () -> Unit,
) {
  DropdownMenuItem(
    modifier = Modifier.testTag(
      TestTags.Menu.item(stringResource(intent.textRes)),
    ),
    text = { Text(text = stringResource(intent.textRes)) },
    leadingIcon = {
      when (intent.icon) {
        is IconWrapper.Image -> Image(
          painter = painterResource((intent.icon as IconWrapper.Image).resourceId),
          colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
          contentDescription = stringResource(intent.textRes),
        )
        is IconWrapper.Icon -> Icon(
          painter = painterResource((intent.icon as IconWrapper.Icon).resourceId),
          contentDescription = stringResource(intent.textRes),
        )
        is IconWrapper.Vector -> Icon(
          imageVector = (intent.icon as IconWrapper.Vector).vector,
          contentDescription = stringResource(intent.textRes),
        )
      }
    },
    onClick = onClick,
  )
}
