package com.divinelink.feature.profile

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.feature.profile.ui.ProfileContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimatedVisibilityScope.ProfileScreen(
  onNavigateToWatchlist: () -> Unit,
  onNavigateToLists: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
  viewModel: ProfileViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        ProfileContent(
          uiState = uiState,
          userInteraction = { userInteraction ->
            when (userInteraction) {
              ProfileUserInteraction.Login -> onNavigateToTMDBAuth()
              ProfileUserInteraction.NavigateToWatchlist -> onNavigateToWatchlist()
            }
          },
        )
      }
    },
  )
}
