package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
  scrollBehavior: TopAppBarScrollBehavior,
  topAppBarColors: TopAppBarColors,
  text: UIText,
  actions: @Composable RowScope.() -> Unit = {},
  isVisible: Boolean = true,
  onNavigateUp: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier.testTag(TestTags.Components.TopAppBar.TOP_APP_BAR),
    scrollBehavior = scrollBehavior,
    colors = topAppBarColors,
    title = {
      AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
      ) {
        Text(
          modifier = Modifier.testTag(TestTags.Components.TopAppBar.TOP_APP_BAR_TITLE),
          text = text.getString(),
          maxLines = 2,
          style = MaterialTheme.typography.titleLarge,
          overflow = TextOverflow.Ellipsis,
        )
      }
    },
    navigationIcon = {
      IconButton(onClick = onNavigateUp) {
        Icon(
          Icons.AutoMirrored.Rounded.ArrowBack,
          stringResource(
            R.string.core_ui_navigate_up_button_content_description,
          ),
        )
      }
    },
    actions = actions,
  )
}
