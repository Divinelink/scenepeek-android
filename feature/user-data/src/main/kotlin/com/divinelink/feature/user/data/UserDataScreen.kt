package com.divinelink.feature.user.data

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.NavigateUpButton
import com.divinelink.feature.user.data.ui.provider.UserDataUiStateParameterProvider
import org.koin.androidx.compose.koinViewModel
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.UserDataScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: UserDataViewModel = koinViewModel(),
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val title by remember {
    mutableIntStateOf(
      when (uiState.section) {
        UserDataSection.Watchlist -> uiR.string.core_ui_section_watchlist
        UserDataSection.Ratings -> uiR.string.core_ui_section_ratings
      },
    )
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .testTag(TestTags.Watchlist.WATCHLIST_SCREEN)
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = topAppBarColor,
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = stringResource(title))
        },
        navigationIcon = {
          NavigateUpButton(onClick = { onNavigate(Navigation.Back) })
        },
      )
    },
    floatingActionButton = {
      uiState.forms.values.elementAt(uiState.selectedTabIndex).let {
        when (it) {
          is UserDataForm.Error.Unauthenticated -> {
            ScaffoldFab(
              icon = Icons.Default.AccountCircle,
              text = stringResource(com.divinelink.core.ui.R.string.core_ui_login),
              expanded = true,
              onClick = { onNavigate(Navigation.TMDBAuthRoute) },
            )
          }
          else -> {
            // No FAB needed for other states
          }
        }
      }
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        UserDataScreenContent(
          uiState = uiState,
          onNavigateToMediaDetails = { onNavigate(it) },
          onRefresh = viewModel::onRefresh,
          onLoadMore = viewModel::onLoadMore,
          onTabSelected = viewModel::onTabSelected,
        )
      }
    },
  )
}

@Composable
@Previews
fun UserDataScreenContentPreview(
  @PreviewParameter(UserDataUiStateParameterProvider::class) uiState: UserDataUiState,
) {
  AppTheme {
    Surface {
      UserDataScreenContent(
        uiState = uiState,
        onRefresh = {},
        onLoadMore = {},
        onTabSelected = {},
        onNavigateToMediaDetails = {},
      )
    }
  }
}
