package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.util.AppSettingsUtil
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.ui.TestTags
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
  onNavigateToJellyseerrSettings: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateUp: () -> Unit,
  viewModel: OnboardingViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigateUp()
    }
  }

  Scaffold(
    modifier = Modifier
      .testTag(TestTags.Onboarding.SCREEN)
      .fillMaxSize(),
    topBar = {
      if (uiState.pages.isNotEmpty() && uiState.pages[uiState.selectedPageIndex].showSkipButton) {
        OnboardingSkipButton(viewModel::onboardingComplete)
      }
    },
  ) {
    Box(
      modifier = Modifier.consumeWindowInsets(it),
    ) {
      OnboardingContent(
        uiState = uiState,
        onCompleteOnboarding = viewModel::onboardingComplete,
        onPageScroll = viewModel::onPageScroll,
        onActionClick = { action ->
          when (action) {
            is OnboardingAction.NavigateToJellyseerrLogin -> onNavigateToJellyseerrSettings()
            is OnboardingAction.NavigateToTMDBLogin -> onNavigateToTMDBLogin()
            is OnboardingAction.NavigateToLinkHandling -> AppSettingsUtil.openAppDetails(context)
          }
        },
      )
    }
  }
}
