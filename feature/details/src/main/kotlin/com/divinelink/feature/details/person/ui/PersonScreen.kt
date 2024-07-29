@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.feature.details.navigation.person.PersonGraph
import com.divinelink.feature.details.navigation.person.PersonNavArguments
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

  AppScaffold(
    topBar = { scrollBehaviour, topAppBarColors ->
      if (uiState is PersonUiState.Success) {
        AppTopAppBar(
          scrollBehaviour = scrollBehaviour,
          topAppBarColors = topAppBarColors,
          text = UIText.StringText(uiState.personDetails.person.name),
          onNavigateUp = navigator::navigateUp,
          actions = {
//            DetailsDropdownMenu(
//              mediaDetails = uiState.dropdownMenuItems,
//              menuOptions = viewState.menuOptions,
//              expanded = showDropdownMenu,
//              requestMedia = requestMedia,
//              onDismissDropdown = { showDropdownMenu = false },
//            )
          },
        )
      }
    },
  ) { paddingValues ->
    when (uiState) {
      PersonUiState.Error -> {
      }
      PersonUiState.Loading -> LoadingContent()
      is PersonUiState.Success -> PersonContent(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
      )
    }
  }
}
