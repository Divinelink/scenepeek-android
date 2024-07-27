@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.feature.details.person.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
  AppScaffold(
    topBar = { scrollBehavior, topAppBarColors ->
      TopAppBar(
        colors = topAppBarColors,
        scrollBehavior = scrollBehavior,
        title = {
          Text("Person")
        },
      )
    },
  ) {
  }
}
