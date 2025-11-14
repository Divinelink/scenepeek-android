package com.divinelink.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_discover
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScaffoldState.DiscoverFab(
  expanded: Boolean,
  onNavigate: (Navigation) -> Unit,
) {
  ScaffoldFab(
    modifier = Modifier.testTag(TestTags.Components.Fab.DISCOVER),
    icon = Icons.Default.SavedSearch,
    text = stringResource(UiString.core_ui_discover),
    expanded = expanded,
    onClick = { onNavigate(Navigation.DiscoverRoute) },
  )
}
