/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.divinelink.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Now in Android navigation bar item with icon and label content slots. Wraps Material 3
 * [NavigationBarItem].
 *
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param modifier Modifier to be applied to this item.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun RowScope.ScenePeekNavigationBarItem(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  alwaysShowLabel: Boolean = true,
  icon: @Composable () -> Unit,
  selectedIcon: @Composable () -> Unit = icon,
  label: @Composable (() -> Unit)? = null,
) {
  NavigationBarItem(
    selected = selected,
    onClick = onClick,
    icon = if (selected) selectedIcon else icon,
    modifier = modifier,
    enabled = enabled,
    label = label,
    alwaysShowLabel = alwaysShowLabel,
    colors = NavigationBarItemDefaults.colors(
      selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
      selectedTextColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
      indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
    ),
  )
}

/**
 * Now in Android navigation bar with content slot. Wraps Material 3 [NavigationBar].
 *
 * @param modifier Modifier to be applied to the navigation bar.
 * @param content Destinations inside the navigation bar. This should contain multiple
 * [NavigationBarItem]s.
 */
@Composable
fun ScenePeekNavigationBar(
  modifier: Modifier = Modifier,
  content: @Composable RowScope.() -> Unit,
) {
  AppNavigationBar(
    modifier = modifier
//      .testTag(TestTags.Components.NAVIGATION_BAR)
      .padding(bottom = 8.dp),
    containerColor = ScenePeekNavigationDefaults.containerColor(),
    contentColor = ScenePeekNavigationDefaults.contentColor(),
    content = content,
  )
}

@Composable
private fun AppNavigationBar(
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.surface,
  contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
  content: @Composable RowScope.() -> Unit,
) {
  Surface(
    color = containerColor.copy(alpha = 0.8f),
    contentColor = contentColor,
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .windowInsetsPadding(windowInsets)
        .heightIn(max = 60.dp)
        .defaultMinSize(minHeight = 60.dp)
        .padding(top = 8.dp)
        .selectableGroup(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      content = content,
    )
  }
}

/**
 * Now in Android navigation rail item with icon and label content slots. Wraps Material 3
 * [NavigationRailItem].
 *
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param modifier Modifier to be applied to this item.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun ScenePeekNavigationRailItem(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  alwaysShowLabel: Boolean = true,
  icon: @Composable () -> Unit,
  selectedIcon: @Composable () -> Unit = icon,
  label: @Composable (() -> Unit)? = null,
) {
  NavigationRailItem(
    selected = selected,
    onClick = onClick,
    icon = if (selected) selectedIcon else icon,
    modifier = modifier,
    enabled = enabled,
    label = label,
    alwaysShowLabel = alwaysShowLabel,
    colors = NavigationRailItemDefaults.colors(
      selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
      selectedTextColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
      indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
    ),
  )
}

/**
 * Now in Android navigation rail with header and content slots. Wraps Material 3 [NavigationRail].
 *
 * @param modifier Modifier to be applied to the navigation rail.
 * @param header Optional header that may hold a floating action button or a logo.
 * @param content Destinations inside the navigation rail. This should contain multiple
 * [NavigationRailItem]s.
 */
@Composable
fun ScenePeekNavigationRail(
  modifier: Modifier = Modifier,
  header: @Composable (ColumnScope.() -> Unit)? = null,
  content: @Composable ColumnScope.() -> Unit,
) {
  NavigationRail(
    modifier = modifier,
    containerColor = Color.Transparent,
    contentColor = ScenePeekNavigationDefaults.contentColor(),
    header = header,
    content = content,
  )
}

/**
 * Now in Android navigation suite scaffold with item and content slots.
 * Wraps Material 3 [NavigationSuiteScaffold].
 *
 * @param modifier Modifier to be applied to the navigation suite scaffold.
 * @param navigationSuiteItems A slot to display multiple items via [ScenePeekNavigationSuiteScope].
 * @param windowAdaptiveInfo The window adaptive info.
 * @param content The app content inside the scaffold.
 */
@Composable
fun ScenePeekNavigationSuiteScaffold(
  navigationSuiteItems: ScenePeekNavigationSuiteScope.() -> Unit,
  modifier: Modifier = Modifier,
  windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
  showNavigationSuite: Boolean,
  content: @Composable (PaddingValues) -> Unit,
) {
  val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo)

  val navigationSuiteItemColors = NavigationSuiteItemColors(
    navigationBarItemColors = NavigationBarItemDefaults.colors(
      selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
      selectedTextColor = ScenePeekNavigationDefaults.selectedTextColor(),
      unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
      indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
    ),
    navigationRailItemColors = NavigationRailItemDefaults.colors(
      selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
      selectedTextColor = ScenePeekNavigationDefaults.selectedTextColor(),
      unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
      indicatorColor = ScenePeekNavigationDefaults.indicatorColor(),
    ),
    navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
      selectedIconColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedIconColor = ScenePeekNavigationDefaults.contentColor(),
      selectedTextColor = ScenePeekNavigationDefaults.selectedItemColor(),
      unselectedTextColor = ScenePeekNavigationDefaults.contentColor(),
    ),
  )

  CustomNavigationSuiteScaffold(
    navigationSuiteItems = {
      ScenePeekNavigationSuiteScope(
        navigationSuiteScope = this,
        navigationSuiteItemColors = navigationSuiteItemColors,
      ).run(navigationSuiteItems)
    },
    showNavigationSuite = showNavigationSuite,
    layoutType = layoutType,
    containerColor = ScenePeekNavigationDefaults.containerColor(),
    navigationSuiteColors = NavigationSuiteDefaults.colors(
      navigationBarContainerColor = ScenePeekNavigationDefaults.containerColor(),
      navigationBarContentColor = ScenePeekNavigationDefaults.contentColor(),
      navigationRailContainerColor = ScenePeekNavigationDefaults.containerColor(),
      navigationRailContentColor = ScenePeekNavigationDefaults.contentColor(),
      navigationDrawerContainerColor = ScenePeekNavigationDefaults.containerColor(),
      navigationDrawerContentColor = ScenePeekNavigationDefaults.contentColor(),
    ),
    modifier = modifier,
    content = {
      content(it)
    },
  )
}

/**
 * A wrapper around [NavigationSuiteScope] to declare navigation items.
 */
class ScenePeekNavigationSuiteScope internal constructor(
  private val navigationSuiteScope: NavigationSuiteScope,
  private val navigationSuiteItemColors: NavigationSuiteItemColors,
) {
  fun item(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
  ) = navigationSuiteScope.item(
    selected = selected,
    onClick = onClick,
    icon = {
      if (selected) {
        selectedIcon()
      } else {
        icon()
      }
    },
    label = label,
    colors = navigationSuiteItemColors,
    modifier = modifier,
  )
}

/**
 * Now in Android navigation default values.
 */
object ScenePeekNavigationDefaults {
  @Composable
  fun contentColor() = MaterialTheme.colorScheme.contentColorFor(containerColor())

  @Composable
  fun containerColor() = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)

  @Composable
  fun selectedTextColor() = MaterialTheme.colorScheme.primary

  @Composable
  fun selectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

  @Composable
  fun indicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
