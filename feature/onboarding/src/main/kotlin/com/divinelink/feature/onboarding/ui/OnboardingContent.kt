package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.coil.BackdropImage
import com.divinelink.core.ui.conditional
import com.divinelink.feature.onboarding.ui.provider.OnboardingUiStatePreviewParameterProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BoxScope.OnboardingContent(
  uiState: OnboardingUiState,
  onActionClick: (OnboardingAction) -> Unit,
  onCompleteOnboarding: () -> Unit,
  onPageScroll: (Int) -> Unit,
) {
  val backdropImages = listOf(
    "https://image.tmdb.org/t/p/original/b3mdmjYTEL70j7nuXATUAD9qgu4.jpg",
    "https://image.tmdb.org/t/p/original/qVBIAcZkK5j6WRq7JehJcOMbdgb.jpg",
    "https://image.tmdb.org/t/p/original/8MtMFngDWvIdRo34rz3ao0BGBAe.jpg",
    "https://image.tmdb.org/t/p/original/4XccmjsOmQZw8S2iW1wvlvmb5v1.jpg",
    "https://image.tmdb.org/t/p/original/lY2DhbA7Hy44fAKddr06UrXWWaQ.jpg",
    "https://image.tmdb.org/t/p/original/jl2YIADk391yc6Qjy9JhgCRkHJk.jpg",
  )

  var backdropImage by remember { mutableIntStateOf(0) }

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedPageIndex) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.pages.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        onPageScroll(page)
      }
  }

  LaunchedEffect(Unit) {
    while (true) {
      delay(6500)

      backdropImage = (backdropImage + 1) % backdropImages.size
    }
  }

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
          condition = uiState.pages.size > 1, // Only add padding if there are pager dots
          ifTrue = {
            val bottomPadding = if (LocalBottomNavigationPadding.current > 0.dp) {
              LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_36
            } else {
              NavigationBarDefaults.windowInsets.asPaddingValues().calculateBottomPadding() +
                MaterialTheme.dimensions.keyline_24
            }

            padding(bottom = bottomPadding)
          },
          ifFalse = {
            padding(
              bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_16,
            )
          },
        ),
      page = uiState.pages[page],
      onCompleteOnboarding = onCompleteOnboarding,
      isLast = page == uiState.pages.size - 1,
      onActionClick = onActionClick,
    )
  }

  PagerDotsIndicator(
    totalPages = uiState.pages.size,
    currentIndex = pagerState.currentPage,
  )
}

@Composable
@Previews
fun OnboardingContentPreview(
  @PreviewParameter(OnboardingUiStatePreviewParameterProvider::class) uiState: OnboardingUiState,
) {
  AppTheme {
    Surface {
      Box {
        OnboardingContent(
          uiState = uiState,
          onActionClick = {},
          onCompleteOnboarding = {},
          onPageScroll = {},
        )
      }
    }
  }
}
