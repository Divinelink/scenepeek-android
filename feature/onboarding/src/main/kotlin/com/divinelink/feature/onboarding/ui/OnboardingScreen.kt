package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.coil.BackdropImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
  onNavigateToJellyseerrSettings: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  viewModel: OnboardingViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val pagerState = rememberPagerState(
    initialPage = 0,
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

  Scaffold {
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
          page = uiState.pages[page],
          onActionClick = {
            //
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
