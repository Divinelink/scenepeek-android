package com.divinelink.core.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekNavigationDefaults
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiTokens

@Composable
fun ScenePeekNavigationBar(
  modifier: Modifier = Modifier,
  containerColor: Color = ScenePeekNavigationDefaults.containerColor(),
  contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
  content: @Composable RowScope.() -> Unit,
) {
  Surface(
    color = containerColor,
    contentColor = contentColor,
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .testTag(TestTags.Components.NAVIGATION_BAR)
        .fillMaxWidth()
        .windowInsetsPadding(windowInsets)
        .heightIn(max = UiTokens.bottomNavHeight)
        .defaultMinSize(minHeight = UiTokens.bottomNavHeight)
        .padding(top = MaterialTheme.dimensions.keyline_8)
        .selectableGroup(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
      content = content,
    )
  }
}
