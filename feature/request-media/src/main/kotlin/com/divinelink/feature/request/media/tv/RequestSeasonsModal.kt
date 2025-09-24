package com.divinelink.feature.request.media.tv

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.request.media.RequestMediaContent
import com.divinelink.feature.request.media.RequestMediaEntryData
import com.divinelink.feature.request.media.RequestMediaViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun RequestSeasonsModal(
  request: JellyseerrRequest?,
  media: MediaItem.Media,
  viewModel: RequestMediaViewModel = koinViewModel(
    key = Uuid.random().toHexString(),
  ) {
    parametersOf(
      RequestMediaEntryData(
        request = request,
        media = media,
      ),
    )
  },
  onDismissRequest: () -> Unit,
  onNavigate: (Navigation) -> Unit,
  onCancelRequest: (requestId: Int) -> Unit,
  onUpdateRequestInfo: (JellyseerrRequest) -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(Unit) {
    viewModel.updatedMediaInfo.collect {
      onUpdateMediaInfo(it)
    }
  }

  LaunchedEffect(Unit) {
    viewModel.updatedRequest.collect {
      onUpdateRequestInfo(it)
    }
  }

  LaunchedEffect(Unit) {
    viewModel.onCancelRequest.collect {
      onCancelRequest(it)
    }
  }

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Modal.REQUEST_SEASONS),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      RequestMediaContent(
        state = uiState,
        onDismissRequest = onDismissRequest,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
      )
    },
  )
}
