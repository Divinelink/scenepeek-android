package com.divinelink.feature.lists.user

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.NavigateUpButton
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.user.ui.ListsContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.ListsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateToList: (ListDetailsRoute) -> Unit,
  viewModel: ListsViewModel = koinViewModel(),
  switchViewButtonViewModel: SwitchViewButtonViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.testTag(TestTags.Lists.SCREEN),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      TopAppBar(
        colors = topAppBarColor,
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = stringResource(R.string.feature_lists_title))
        },
        navigationIcon = { NavigateUpButton(onClick = onNavigateUp) },
      )
    },
    floatingActionButton = {
      if (uiState.error is BlankSlateState.Unauthenticated) {
        ScaffoldFab(
          icon = Icons.Default.AccountCircle,
          text = stringResource(com.divinelink.core.ui.R.string.core_ui_login),
          expanded = true,
          onClick = onNavigateToTMDBLogin,
        )
      }
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        ListsContent(
          uiState = uiState,
          action = { userInteraction ->
            when (userInteraction) {
              ListsAction.LoadMore -> viewModel.onLoadMore()
              ListsAction.Refresh -> viewModel.onRefresh()
              is ListsAction.OnListClick -> onNavigateToList(
                ListDetailsRoute(
                  id = userInteraction.id,
                  name = userInteraction.name,
                  backdropPath = userInteraction.backdropPath,
                  description = userInteraction.description,
                  public = userInteraction.public,
                ),
              )
              ListsAction.SwitchViewMode -> switchViewButtonViewModel.switchViewMode(
                section = ViewableSection.LISTS,
              )
            }
          },
        )
      }
    },
  )
}
