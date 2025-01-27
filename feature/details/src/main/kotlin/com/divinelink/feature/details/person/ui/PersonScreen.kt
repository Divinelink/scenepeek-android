@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.core.ui.nestedscroll.rememberCollapsingContentNestedScrollConnection
import com.divinelink.feature.details.navigation.person.PersonGraph
import com.divinelink.feature.details.person.ui.tab.PersonTab
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination<PersonGraph>(
  start = true,
  navArgs = PersonNavArguments::class,
  deepLinks = [
    DeepLink(uriPattern = "https://www.themoviedb.org/person/{id}-.*"),
    DeepLink(uriPattern = "https://www.themoviedb.org/person/{id}"),
  ],
)
fun PersonScreen(
  navigator: DestinationsNavigator,
  viewModel: PersonViewModel = koinViewModel(),
) {
  val uiState = viewModel.uiState.collectAsState().value
  var showDropdownMenu by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()
  val connection = rememberCollapsingContentNestedScrollConnection(maxHeight = 256.dp)

  val isAppBarVisible = remember {
    derivedStateOf { connection.currentSize < 1.dp }
  }

  val aboutForm = remember(uiState) {
    uiState.forms.getOrElse(PersonTab.ABOUT.order) { null } as? PersonForm.About
  }
  val personDetails = aboutForm?.personDetails?.takeIf { it is PersonDetailsUiState.Data }

  AppScaffold(
    topBar = { scrollBehavior, topAppBarColors ->
      if (personDetails is PersonDetailsUiState.Data) {
        AppTopAppBar(
          scrollBehavior = scrollBehavior,
          topAppBarColors = topAppBarColors,
          text = UIText.StringText(personDetails.personDetails.person.name),
          isVisible = isAppBarVisible.value,
          onNavigateUp = navigator::navigateUp,
          actions = {
            IconButton(
              modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
              onClick = { showDropdownMenu = !showDropdownMenu },
            ) {
              Icon(Icons.Outlined.MoreVert, "More")
            }

            PersonDropdownMenu(
              person = personDetails.personDetails.person,
              expanded = showDropdownMenu,
              onDismissDropdown = { showDropdownMenu = false },
            )
          },
        )
      }
    },
  ) {
    when {
      uiState.isError -> {
        // TODO Add error content
      }
      uiState.isLoading -> LoadingContent()
      else -> PersonContent(
        uiState = uiState,
        connection = connection,
        lazyListState = lazyListState,
        scope = scope,
        onMediaClick = { mediaItem ->
          navigator.navigate(
            DetailsScreenDestination(
              DetailsNavArguments(
                id = mediaItem.id,
                mediaType = mediaItem.mediaType.value,
                isFavorite = null,
              ),
            ),
          )
        },
        onTabSelected = viewModel::onTabSelected,
        onUpdateLayoutStyle = viewModel::onUpdateLayoutStyle,
        onApplyFilter = viewModel::onApplyFilter,
        personDetails = personDetails as PersonDetailsUiState.Data,
        movies = uiState.filteredCredits[PersonTab.MOVIES.order] ?: emptyMap(),
        tvShows = uiState.filteredCredits[PersonTab.TV_SHOWS.order] ?: emptyMap(),
      )
    }
  }
}
