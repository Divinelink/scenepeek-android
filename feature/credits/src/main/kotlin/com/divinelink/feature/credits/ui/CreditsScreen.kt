package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.feature.credits.R
import com.divinelink.feature.credits.navigation.CreditsGraph
import com.divinelink.feature.credits.navigation.CreditsNavArguments
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.divinelink.core.ui.R as uiR

// TODO Check we could add deep link
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination<CreditsGraph>(
  start = true,
  navArgs = CreditsNavArguments::class,
)
fun CreditsScreen(
  navigator: DestinationsNavigator,
  viewModel: CreditsViewModel = hiltViewModel(),
) {
  val uiState = viewModel.uiState.collectAsState().value

  AppScaffold(
    topBar = { scrollBehaviour, color ->
      TopAppBar(
        scrollBehavior = scrollBehaviour,
        colors = color,
        title = {
          Text(
            text = stringResource(id = R.string.feature_credits_cast_and_crew_title),
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
          )
        },
        navigationIcon = {
          IconButton(
            onClick = navigator::navigateUp,
          ) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              stringResource(uiR.string.core_ui_navigate_up_button_content_description),
            )
          }
        },

      )
    },
  ) { paddingValues ->
    CreditsContent(
      modifier = Modifier.padding(paddingValues),
      state = uiState,
      onTabSelected = viewModel::onTabSelected,
      onPersonSelected = {
        // TODO navigate to person details
      },
    )
  }
}
