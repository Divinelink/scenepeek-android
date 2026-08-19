package com.divinelink.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DataState
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.permission.canManageRequests
import com.divinelink.core.model.jellyseerr.permission.canRequestAdvanced
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.skeleton.RequestItemSkeleton
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.RequestsViewModel
import com.divinelink.feature.requests.ui.RequestMediaItem
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeerrItems(
  filter: MediaRequestFilter,
  viewModel: RequestsViewModel = koinViewModel { parametersOf(filter) },
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

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
              Text("You're all caught up!")
            }
          } else {
            items(
              items = (state.data as DataState.Data).data,
              key = { it.request.id },
            ) { request ->
              RequestMediaItem(
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
                canManageRequest = state.permissions.canManageRequests,
                canRequestAdvanced = state.permissions.canRequestAdvanced,
              )
            }
          }
        }
      }
    }
  }
}
