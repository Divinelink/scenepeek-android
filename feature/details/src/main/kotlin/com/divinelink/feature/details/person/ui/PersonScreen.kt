@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.feature.details.person.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonScreen(
  onNavigateUp: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: PersonViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  var showDropdownMenu by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()

  var isAppBarVisible by remember { mutableStateOf(false) }

  val personDetails by remember(uiState.aboutForm) {
    derivedStateOf { uiState.aboutForm?.personDetails }
  }

  val containerColor by animateColorAsState(
    targetValue = if (isAppBarVisible) {
      MaterialTheme.colorScheme.surface
    } else {
      Color.Transparent
    },
    animationSpec = tween(durationMillis = 0),
    label = "appBarContainerColor",
  )

  rememberScaffoldState(
    animatedVisibilityScope = animatedVisibilityScope,
  ).PersistentScaffold(
    topBar = {
      if (personDetails is PersonDetailsUiState.Data) {
        AppTopAppBar(
          modifier = Modifier.background(color = containerColor),
          scrollBehavior = scrollBehavior,
          text = UIText.StringText(
            (personDetails as PersonDetailsUiState.Data).personDetails.person.name,
          ),
          isVisible = isAppBarVisible,
          onNavigateUp = onNavigateUp,
          topAppBarColors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
          ),
          actions = {
            IconButton(
              modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
              onClick = { showDropdownMenu = !showDropdownMenu },
            ) {
              Icon(Icons.Outlined.MoreVert, "More")
            }

            PersonDropdownMenu(
              person = (personDetails as PersonDetailsUiState.Data).personDetails.person,
              expanded = showDropdownMenu,
              onDismissDropdown = { showDropdownMenu = false },
            )
          },
        )
      }
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        when {
          uiState.isError -> {
            // TODO Add error content
          }
          uiState.isLoading -> LoadingContent()
          else -> PersonContent(
            uiState = uiState,
            lazyListState = lazyListState,
            scope = scope,
            onMediaClick = { mediaItem ->
              onNavigateToDetails(
                DetailsRoute(
                  id = mediaItem.id,
                  mediaType = mediaItem.mediaType,
                  isFavorite = null,
                ),
              )
            },
            onTabSelected = viewModel::onTabSelected,
            onUpdateLayoutStyle = viewModel::onUpdateLayoutStyle,
            onApplyFilter = viewModel::onApplyFilter,
            onShowTitle = { isAppBarVisible = it },
          )
        }
      }
    },
  )
}
