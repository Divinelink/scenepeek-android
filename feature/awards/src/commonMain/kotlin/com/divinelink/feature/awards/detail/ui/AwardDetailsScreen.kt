package com.divinelink.feature.awards.detail.ui

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
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.feature.awards.detail.AwardDetailsAction
import com.divinelink.feature.awards.detail.AwardDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.AwardDetailsScreen(
  route: Navigation.AwardDetailsRoute,
  onNavigate: (Navigation) -> Unit,
  viewModel: AwardDetailsViewModel = koinViewModel { parametersOf(route) },
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
        text = UIText.StringText(uiState.ceremony.title),
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

        AwardDetailsContent(
          uiState = uiState,
          action = { action ->
            when (action) {
              is AwardDetailsAction.OnCategoryClick -> onNavigate(
                Navigation.AwardCategoryRoute(
                  category = action.category,
                  ceremonyId = action.ceremonyId,
                ),
              )
              AwardDetailsAction.OnRetry -> viewModel.onAction(action)
            }
          },
        )
      }
    },
  )
}
