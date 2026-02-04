package com.divinelink.core.ui.collapsingheader.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.PosterImage
import com.divinelink.core.ui.collapsingheader.CollapsingHeaderState
import com.divinelink.core.ui.components.details.BackdropImage
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.mediaImageDropShadow
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.CollapsibleHeaderContent(
  collapsingHeaderState: CollapsingHeaderState,
  backdropPath: String?,
  posterPath: String?,
  onBackdropLoaded: () -> Unit,
  visibilityScope: AnimatedVisibilityScope,
  onNavigateToMediaPoster: (String) -> Unit,
  content: @Composable () -> Unit,
) {
  Box(
    modifier = Modifier.offset {
      IntOffset(
        x = 0,
        y = -collapsingHeaderState.translation.roundToInt(),
      )
    },
  ) {
    BackdropImage(
      path = backdropPath,
      onBackdropLoaded = onBackdropLoaded,
    )

    Column(
      modifier = Modifier
        .verticalScroll(state = rememberScrollState())
        .testTag(TestTags.Details.COLLAPSIBLE_CONTENT)
        .padding(MaterialTheme.dimensions.keyline_16)
        .conditional(
          condition = backdropPath?.isNotBlank() == true,
          ifTrue = { padding(top = MaterialTheme.dimensions.keyline_56) },
        )
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        PosterImage(
          modifier = Modifier
            .sharedElement(
              sharedContentState = rememberSharedContentState(
                SharedElementKeys.MediaPoster(posterPath ?: ""),
              ),
              animatedVisibilityScope = visibilityScope,
            )
            .mediaImageDropShadow()
            .height(MaterialTheme.dimensions.posterSizeSmall)
            .aspectRatio(2f / 3f),
          path = posterPath,
          quality = ImageQuality.QUALITY_342,
          onClick = { onNavigateToMediaPoster(it) },
        )

        content()
      }
    }
  }
}
