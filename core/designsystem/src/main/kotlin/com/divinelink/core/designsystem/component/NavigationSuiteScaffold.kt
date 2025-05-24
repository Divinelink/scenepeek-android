package com.divinelink.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.divinelink.core.designsystem.theme.dimensions

internal val WindowAdaptiveInfoDefault
  @Composable get() = currentWindowAdaptiveInfo()

@Composable
fun CustomNavigationSuiteScaffold(
  navigationSuiteItems: NavigationSuiteScope.() -> Unit,
  modifier: Modifier = Modifier,
  layoutType: NavigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
    WindowAdaptiveInfoDefault,
  ),
  showNavigationSuite: Boolean,
  navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
  containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
  contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
  content: @Composable (PaddingValues) -> Unit,
) {
  var navigationSuiteSize by remember { mutableIntStateOf(0) }
  val navigationSuiteSizeDp = with(LocalDensity.current) {
    navigationSuiteSize.toDp()
  }

  Surface(
    modifier = modifier,
    color = containerColor,
    contentColor = contentColor,
  ) {
    Box {
      Box(
        modifier = Modifier
          .padding(
            start = when (layoutType) {
              NavigationSuiteType.NavigationBar -> MaterialTheme.dimensions.keyline_0
              NavigationSuiteType.NavigationRail -> navigationSuiteSizeDp
              NavigationSuiteType.NavigationDrawer -> navigationSuiteSizeDp
              else -> MaterialTheme.dimensions.keyline_0
            },
          )
          .consumeWindowInsets(
            when (layoutType) {
              NavigationSuiteType.NavigationBar ->
                NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)
              NavigationSuiteType.NavigationRail ->
                NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Start)
              NavigationSuiteType.NavigationDrawer ->
                DrawerDefaults.windowInsets.only(WindowInsetsSides.Start)
              else -> WindowInsets(0, 0, 0, 0)
            },
          ),
      ) {
        content(
          PaddingValues(
            start = if (layoutType == NavigationSuiteType.NavigationRail ||
              layoutType == NavigationSuiteType.NavigationDrawer
            ) {
              navigationSuiteSizeDp
            } else {
              MaterialTheme.dimensions.keyline_0
            },
            bottom = if (layoutType == NavigationSuiteType.NavigationBar) {
              navigationSuiteSizeDp
            } else {
              MaterialTheme.dimensions.keyline_0
            },
          ),
        )
      }

      NavigationSuiteScaffoldLayout(
        navigationSuite = {
          Column {
            AnimatedVisibility(
              visible = showNavigationSuite,
              modifier = Modifier.onSizeChanged {
                when (layoutType) {
                  NavigationSuiteType.NavigationBar -> navigationSuiteSize = it.height
                  NavigationSuiteType.NavigationRail -> navigationSuiteSize = it.width
                  NavigationSuiteType.NavigationDrawer -> navigationSuiteSize = it.width
                  else -> {}
                }
              },
            ) {
              NavigationSuite(
                modifier = Modifier.wrapContentHeight(),
                layoutType = layoutType,
                colors = NavigationSuiteDefaults.colors(
                  navigationBarContentColor = navigationSuiteColors.navigationBarContentColor,
                  navigationRailContentColor = navigationSuiteColors.navigationRailContentColor,
                  navigationDrawerContentColor = navigationSuiteColors.navigationDrawerContentColor,
                  navigationBarContainerColor = ScenePeekNavigationDefaults.containerColor(),
                  navigationRailContainerColor = ScenePeekNavigationDefaults.containerColor(),
                  navigationDrawerContainerColor = ScenePeekNavigationDefaults.containerColor(),
                ),
                content = navigationSuiteItems,
              )
            }
          }
        },
        layoutType = layoutType,
        content = {
          // The content is already provided in the box above
        },
      )
    }
  }
}
