package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.conditional
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroModalBottomSheet(
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  viewModel: IntroViewModel = koinViewModel(),
) {
  val intentManager = LocalIntentManager.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
    confirmValueChange = {
      if (uiState.isFirstLaunch) {
        it != SheetValue.Hidden
      } else {
        true
      }
    },
  )
  val properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false)

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigate(Navigation.Back)
    }
  }

  ModalBottomSheet(
    modifier = modifier
      .conditional(
        condition = uiState.isFirstLaunch,
        ifTrue = { fillMaxSize() },
      )
      .conditional(
        condition = uiState.isFirstLaunch,
        ifTrue = { consumeWindowInsets(WindowInsets.systemBars) },
      ),
    shape = MaterialTheme.shapes.extraLarge,
    properties = properties,
    onDismissRequest = { viewModel.onboardingComplete() },
    sheetState = sheetState,
    content = {
      OnboardingContent(
        uiState = uiState,
        onDismiss = { viewModel.onboardingComplete() },
        onActionClick = { action ->
          when (action) {
            is OnboardingAction.NavigateToJellyseerrLogin -> onNavigate(
              Navigation.JellyseerrSettingsRoute(withNavigationBar = false),
            )
            OnboardingAction.NavigateToLinkHandling -> {
              intentManager.navigateToAppSettings()
            }
            is OnboardingAction.NavigateToTMDBLogin -> onNavigate(
              Navigation.TMDBAuthRoute,
            )
          }
        },
      )
    },
  )
}
