package com.divinelink.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DataState
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.skeleton.RequestItemSkeleton
import com.divinelink.feature.request.media.RequestMediaModal
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsViewModel
import com.divinelink.feature.requests.resources.Res
import com.divinelink.feature.requests.resources.feature_requests_no_requests_available
import com.divinelink.feature.requests.ui.HomeRequestMediaItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeerrContent(
  filter: MediaRequestFilter,
  viewModel: RequestsViewModel = koinViewModel { parametersOf(filter) },
  onNavigate: (Navigation) -> Unit,
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var showRequestModal by remember {
    mutableStateOf<Pair<MediaItem.Media, JellyseerrRequest>?>(null)
  }
  var isFirstLoad by rememberSaveable { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    viewModel.displayRequestModal.collect {
      showRequestModal = it
    }
  }

  LaunchedEffect(lifecycleOwner) {
    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
      if (isFirstLoad) {
        isFirstLoad = false
      } else {
        viewModel.onAction(RequestsAction.Refresh)
      }
    }
  }

  showRequestModal?.let { request ->
    RequestMediaModal(
      media = request.first,
      request = request.second,
      mediaType = request.first.mediaType,
      onDismissRequest = { showRequestModal = null },
      onUpdateRequestInfo = { viewModel.onAction(RequestsAction.UpdateRequestInfo(it)) },
      onCancelRequest = { viewModel.onAction(RequestsAction.OnRequestCanceled(it)) },
      onNavigate = onNavigate,
    )
  }

  LazyRow(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    if (state.error != null) {
      item {
        MediaRowError(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
          onClick = { viewModel.onAction(RequestsAction.Refresh) },
        )
      }
    } else {
      when (state.data) {
        DataState.Initial -> items(5) {
          RequestItemSkeleton(
            modifier = Modifier.conditional(
              condition = rememberIsLandscape(),
              ifTrue = { fillParentMaxWidth(0.5f) },
              ifFalse = { fillParentMaxWidth(0.8f) },
            ),
          )
        }
        is DataState.Data -> {
          val requests = (state.data as DataState.Data).data

          if (requests.isEmpty()) {
            item {
              Text(
                modifier = Modifier.padding(MaterialTheme.dimensions.keyline_4),
                text = stringResource(Res.string.feature_requests_no_requests_available),
                style = MaterialTheme.typography.titleSmall,
              )
            }
          } else {
            items(
              items = (state.data as DataState.Data).data,
              key = { it.request.id },
            ) { request ->
              HomeRequestMediaItem(
                modifier = Modifier
                  .conditional(
                    condition = rememberIsLandscape(),
                    ifTrue = { fillParentMaxWidth(0.5f) },
                    ifFalse = { fillParentMaxWidth(0.8f) },
                  )
                  .onVisibilityChanged(minFractionVisible = 0.1f) { visible ->
                    if (visible) {
                      viewModel.onAction(RequestsAction.FetchMediaItem(request))
                    }
                  },
                item = request,
                onClick = {
                  onNavigate(
                    Navigation.DetailsRoute(
                      mediaType = it.mediaType.value,
                      id = it.id,
                      isFavorite = it.saved,
                    ),
                  )
                },
                onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
                onAction = viewModel::onAction,
              )
            }
          }
        }
      }
    }
  }
}
