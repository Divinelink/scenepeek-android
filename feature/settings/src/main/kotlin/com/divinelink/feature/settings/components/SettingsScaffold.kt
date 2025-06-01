package com.divinelink.feature.settings.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
  modifier: Modifier = Modifier,
  title: String,
  onNavigationClick: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigationIconPainter: ImageVector = Icons.AutoMirrored.Rounded.ArrowBack,
  @StringRes navigationContentDescription: Int =
    uiR.string.core_ui_navigate_up_button_content_description,
  floatingActionButton: @Composable ScaffoldState.() -> Unit = {},
  content: @Composable (PaddingValues) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  rememberScaffoldState(
    animatedVisibilityScope = animatedVisibilityScope,
  ).PersistentScaffold(
    modifier = modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TestTags.Settings.TOP_APP_BAR),
        colors = topAppBarColors(
          scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
          Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
          )
        },
        navigationIcon = {
          IconButton(
            modifier = Modifier.testTag(TestTags.Settings.NAVIGATION_ICON),
            onClick = onNavigationClick,
          ) {
            Icon(
              imageVector = navigationIconPainter,
              contentDescription = stringResource(id = navigationContentDescription),
            )
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
    floatingActionButton = {
      floatingActionButton()
    },
  ) {
    content(it)
  }
}
