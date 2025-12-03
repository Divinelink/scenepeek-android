package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.coil.BackdropImage
import com.divinelink.feature.onboarding.ui.provider.OnboardingUiStatePreviewParameterProvider
import com.divinelink.feature.onboarding.ui.sections.FeatureSection
import com.divinelink.feature.onboarding.ui.sections.SectionHeader
import com.divinelink.feature.onboarding.ui.sections.SectionSecondaryHeader
import com.divinelink.feature.onboarding.ui.sections.SectionText
import com.divinelink.feature.onboarding.ui.sections.SkipIntroButton
import com.divinelink.feature.onboarding.ui.sections.WhatsNewSection
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun OnboardingContent(
  uiState: OnboardingUiState,
  onActionClick: (OnboardingAction) -> Unit,
  onDismiss: () -> Unit,
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

  LaunchedEffect(Unit) {
    while (true) {
      delay(8000)

      backdropImage = (backdropImage + 1) % backdropImages.size
    }
  }

  Box {
    if (uiState.isFirstLaunch) {
      BackdropImage(
        modifier = Modifier
          .fillMaxSize()
          .blurEffect(
            radiusX = 3f,
            radiusY = 3f,
          )
          .graphicsLayer { alpha = 0.15f },
        url = backdropImages[backdropImage],
      )
    }
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .testTag(TestTags.Components.SCROLLABLE_CONTENT),
      contentPadding = PaddingValues(
        horizontal = MaterialTheme.dimensions.keyline_8,
        vertical = MaterialTheme.dimensions.keyline_16,
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      items(
        items = uiState.sections,
      ) { item ->
        when (item) {
          is IntroSection.Header -> SectionHeader(item)
          is IntroSection.SecondaryHeader -> SectionSecondaryHeader(item)
          is IntroSection.Feature -> FeatureSection(
            section = item,
            onAction = onActionClick,
          )
          is IntroSection.Text -> SectionText(item)
          is IntroSection.WhatsNew -> WhatsNewSection(item)
          IntroSection.Spacer -> Spacer(
            modifier = Modifier
              .fillMaxWidth()
              .height(MaterialTheme.dimensions.keyline_8),
          )
          IntroSection.Divider -> HorizontalDivider(modifier = Modifier.fillMaxWidth())
          IntroSection.GetStartedButton -> SkipIntroButton(onDismiss)
        }
      }
    }
  }
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
          onDismiss = {},
        )
      }
    }
  }
}
