package com.divinelink.feature.credits.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.ObfuscateSpoilersButton
import com.divinelink.feature.credits.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.CreditsScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: CreditsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        topAppBarColors = topAppBarColor,
        text = UIText.ResourceText(R.string.feature_credits_cast_and_crew_title),
        actions = {
          ObfuscateSpoilersButton(
            obfuscated = uiState.obfuscateSpoilers,
            onClick = viewModel::onObfuscateSpoilers,
          )
        },
        onNavigateUp = { onNavigate(Navigation.Back) },
      )
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    navigationRail = {
      PersistentNavigationRail()
    },
  ) {
    Column {
      Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

      CreditsContent(
        state = uiState,
        onTabSelected = viewModel::onTabSelected,
        onPersonSelected = { onNavigate(it.toPersonRoute()) },
      )
    }
  }
}
