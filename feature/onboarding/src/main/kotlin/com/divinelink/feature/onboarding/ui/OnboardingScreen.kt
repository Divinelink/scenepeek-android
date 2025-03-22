package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.coil.BackdropImage
import com.divinelink.core.ui.conditional
import com.divinelink.feature.onboarding.OnboardingAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
  onNavigateToJellyseerrSettings: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateUp: () -> Unit,
  viewModel: OnboardingViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedPageIndex) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.pages.size },
  )

  val backdropImages = listOf(
    "https://image.tmdb.org/t/p/original/b3mdmjYTEL70j7nuXATUAD9qgu4.jpg",
    "https://image.tmdb.org/t/p/original/qVBIAcZkK5j6WRq7JehJcOMbdgb.jpg",
    "https://image.tmdb.org/t/p/original/8MtMFngDWvIdRo34rz3ao0BGBAe.jpg",
    "https://image.tmdb.org/t/p/original/4XccmjsOmQZw8S2iW1wvlvmb5v1.jpg",
    "https://image.tmdb.org/t/p/original/lY2DhbA7Hy44fAKddr06UrXWWaQ.jpg",
    "https://image.tmdb.org/t/p/original/jl2YIADk391yc6Qjy9JhgCRkHJk.jpg",
  )

  var backdropImage by remember { mutableIntStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(6500)

      backdropImage = (backdropImage + 1) % backdropImages.size
    }
  }

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigateUp()
    }
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        viewModel.onPageScroll(page)
      }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (uiState.pages.isNotEmpty() && uiState.pages[pagerState.currentPage].showSkipButton) {
        OnboardingSkipButton(viewModel::onboardingComplete)
      }
    },
  ) {
    Box(modifier = Modifier.consumeWindowInsets(it)) {
      BackdropImage(
        modifier = Modifier
          .fillMaxSize()
          .blurEffect(
            radiusX = 3f,
            radiusY = 3f,
          )
          .graphicsLayer { alpha = 0.3f },
        url = backdropImages[backdropImage],
      )

      HorizontalPager(
        state = pagerState,
      ) { page ->
        OnboardingItem(
          modifier = Modifier
            .fillMaxSize()
            .conditional(
              condition = uiState.pages.size > 1,
              ifTrue = {
                padding(
                  bottom = LocalBottomNavigationPadding.current +
                    MaterialTheme.dimensions.keyline_64,
                )
              },
              ifFalse = {
                padding(
                  bottom = LocalBottomNavigationPadding.current +
                    MaterialTheme.dimensions.keyline_16,
                )
              },
            ),
          page = uiState.pages[page],
          onCompleteOnboarding = viewModel::onboardingComplete,
          isLast = page == uiState.pages.size - 1,
          onActionClick = { action ->
            when (action) {
              is OnboardingAction.NavigateToJellyseerrLogin -> onNavigateToJellyseerrSettings()
              is OnboardingAction.NavigateToTMDBLogin -> onNavigateToTMDBLogin()
            }
          },
        )
      }

      PagerDotsIndicator(
        totalPages = uiState.pages.size,
        currentIndex = pagerState.currentPage,
      )
    }
  }
}
