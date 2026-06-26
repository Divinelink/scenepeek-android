package com.divinelink.feature.awards.popular.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.awards
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AwardDetailsRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.feature.awards.popular.AwardsAction
import com.divinelink.feature.awards.popular.AwardsViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.divinelink.core.model.resources.Res as ModelRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.AwardsScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: AwardsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        text = UIText.ResourceText(ModelRes.string.awards),
        onNavigateUp = { onNavigate(Navigation.Back) },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        AwardsContent(
          uiState = uiState,
          action = { action ->
            when (action) {
              AwardsAction.OnRetry -> viewModel.onAction(action)
              is AwardsAction.OnCeremonyClick -> onNavigate(AwardDetailsRoute(action.ceremony))
            }
          },
        )
      }
    },
  )
}
