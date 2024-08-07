@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.feature.details.navigation.person.PersonGraph
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
  viewModel: PersonViewModel = hiltViewModel(),
) {
  val uiState = viewModel.uiState.collectAsState().value
  var showDropdownMenu by remember { mutableStateOf(false) }

  AppScaffold(
    topBar = { scrollBehaviour, topAppBarColors ->
      if (
        uiState is PersonUiState.Success &&
        uiState.personDetails is PersonDetailsUiState.Visible
      ) {
        AppTopAppBar(
          scrollBehaviour = scrollBehaviour,
          topAppBarColors = topAppBarColors,
          text = UIText.StringText(uiState.personDetails.personDetails.person.name),
          onNavigateUp = navigator::navigateUp,
          actions = {
            IconButton(
              modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
              onClick = { showDropdownMenu = !showDropdownMenu },
            ) {
              Icon(Icons.Outlined.MoreVert, "More")
            }

            PersonDropdownMenu(
              person = uiState.personDetails.personDetails.person,
              expanded = showDropdownMenu,
              onDismissDropdown = { showDropdownMenu = false },
            )
          },
        )
      }
    },
  ) { paddingValues ->
    when (uiState) {
      PersonUiState.Error -> {
        // TODO Add error content
      }
      PersonUiState.Loading -> LoadingContent()
      is PersonUiState.Success -> {
        if (uiState.personDetails is PersonDetailsUiState.Loading) {
          LoadingContent()
        } else {
          PersonContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onMediaClick = {
              navigator.navigate(
                DetailsScreenDestination(
                  DetailsNavArguments(
                    id = it.id,
                    mediaType = it.mediaType.value,
                    isFavorite = null,
                  ),
                ),
              )
            },
          )
        }
      }
    }
  }
}
