package com.divinelink.feature.add.to.account.rate

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.domain.components.submit.SubmitRatingAction
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.modal.ScenePeekBottomSheet
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.add.to.account.rate.ui.SubmitRatingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateModalBottomSheet(
  modifier: Modifier = Modifier,
  isVisible: Boolean,
  route: Navigation.SubmitRatingRoute,
  viewModel: SubmitRatingViewModel = koinViewModel {
    parametersOf(route)
  },
  onSetLoading: (Boolean, Float?) -> Unit,
  onDismissRequest: () -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  SnackbarMessageHandler(
    snackbarMessage = state.snackbarMessage,
    onShowMessage = { onSetLoading(false, state.value) },
    onDismissSnackbar = { viewModel.action(SubmitRatingAction.DismissSnackbar) },
  )

  LaunchedEffect(Unit) {
    viewModel.navigateToLogin.collectLatest {
      onNavigate(Navigation.TMDBAuthRoute)
    }
  }

  LaunchedEffect(route) {
    viewModel.init(route)
  }

  ScenePeekBottomSheet(
    isVisible = isVisible,
    onDismissRequest = onDismissRequest,
  ) {
    RateDialogContent(
      modifier = modifier,
      value = state.value ?: 0f,
      mediaTitle = state.title,
      onSubmitRate = {
        viewModel.action(SubmitRatingAction.SubmitRating(it.toFloat()))
        onSetLoading(true, it.toFloat())
        onDismissRequest()
      },
      onClearRate = {
        viewModel.action(SubmitRatingAction.ClearRating)
        onSetLoading(true, null)
        onDismissRequest()
      },
      canClearRate = state.value != null,
    )
  }
}
