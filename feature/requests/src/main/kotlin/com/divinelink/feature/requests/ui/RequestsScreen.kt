package com.divinelink.feature.requests.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.NavigateUpButton
import com.divinelink.feature.request.media.RequestMediaModal
import com.divinelink.feature.requests.R
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.RequestsScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: RequestsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var showRequestModal by remember {
    mutableStateOf<Pair<MediaItem.Media, JellyseerrRequest>?>(null)
  }

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  LaunchedEffect(Unit) {
    viewModel.displayRequestModal.collect {
      showRequestModal = it
    }
  }

  showRequestModal?.let { request ->
    RequestMediaModal(
      media = request.first,
      request = request.second,
      mediaType = request.first.mediaType,
      seasons = emptyList(),
      onDismissRequest = { showRequestModal = null },
      onUpdateRequestInfo = { viewModel.onAction(RequestsAction.UpdateRequestInfo(it)) },
      onNavigate = onNavigate,
    )
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.testTag(TestTags.Jellyseerr.Requests.SCAFFOLD),
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
          Text(
            text = stringResource(R.string.feature_requests_title),
            style = LocalTextStyle.current.copy(
              brush = MaterialTheme.colors.jellyseerrGradientBrush,
            ),
          )
        },
        navigationIcon = {
          NavigateUpButton(onClick = { onNavigate(Navigation.Back) })
        },
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        RequestsContent(
          state = uiState,
          onNavigate = onNavigate,
          action = viewModel::onAction,
        )
      }
    },
  )
}
