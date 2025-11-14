package com.divinelink.feature.details.media.ui.ratings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.provider.RatingCountParameterProvider
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllRatingsModalBottomSheet(
  modifier: Modifier = Modifier,
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
  ratingCount: RatingCount,
  onClick: (RatingSource) -> Unit,
) {
  ModalBottomSheet(
    modifier = modifier.testTag(TestTags.Rating.ALL_RATINGS_BOTTOM_SHEET),
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    AllRatingsContent(
      onClick = onClick,
      ratingCount = ratingCount,
    )
    // TODO KMP
//    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Previews
@Composable
private fun AllRatingsModalBottomSheetPreview(
  @PreviewParameter(RatingCountParameterProvider::class) ratingCount: RatingCount,
) {
  AppTheme {
    Surface {
      AllRatingsModalBottomSheet(
        onDismissRequest = {},
        ratingCount = ratingCount,
        onClick = {},
        sheetState = rememberModalBottomSheetState(
          skipPartiallyExpanded = true,
        ),
      )
    }
  }
}
