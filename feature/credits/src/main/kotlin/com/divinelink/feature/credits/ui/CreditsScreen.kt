package com.divinelink.feature.credits.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.details.Person
import com.divinelink.core.navigation.arguments.CreditsNavArguments
import com.divinelink.core.ui.components.ObfuscateSpoilersButton
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.feature.credits.R
import com.divinelink.feature.credits.navigation.CreditsGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
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
  viewModel: CreditsViewModel = koinViewModel(),
  onNavigateToPersonDetails: (Person) -> Unit,
) {
  val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

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
        actions = {
          ObfuscateSpoilersButton(
            obfuscated = uiState.obfuscateSpoilers,
            onClick = viewModel::onObfuscateSpoilers,
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
  ) {
    CreditsContent(
      state = uiState,
      onTabSelected = viewModel::onTabSelected,
      onPersonSelected = { onNavigateToPersonDetails(it) },
    )
  }
}
