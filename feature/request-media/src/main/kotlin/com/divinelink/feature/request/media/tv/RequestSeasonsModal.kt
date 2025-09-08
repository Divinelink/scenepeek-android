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
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.request.media.RequestMediaContent
import com.divinelink.feature.request.media.RequestMediaViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestSeasonsModal(
  seasons: List<Season>,
  media: MediaItem.Media,
  viewModel: RequestMediaViewModel = koinViewModel {
    parametersOf(media)
  },
  onDismissRequest: () -> Unit,
  onNavigate: (Navigation) -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(seasons) {
    viewModel.updateSeasons(seasons)
  }

  LaunchedEffect(Unit) {
    viewModel.updatedMediaInfo.collect {
      onUpdateMediaInfo(it)
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
