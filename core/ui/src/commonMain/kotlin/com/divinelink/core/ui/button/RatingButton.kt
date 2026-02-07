package com.divinelink.core.ui.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.rating.YourRatingText
import com.divinelink.core.ui.resources.Res
import com.divinelink.core.ui.resources.core_ui_add_rating
import org.jetbrains.compose.resources.stringResource

@Composable
fun RatingButton(
  modifier: Modifier = Modifier,
  accountRating: Int?,
  onClick: () -> Unit,
  isLoading: Boolean,
) {
  ElevatedButton(
    shape = MaterialTheme.shape.large,
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = MaterialTheme.dimensions.keyline_2,
    ),
    modifier = modifier.testTag(TestTags.Details.RATE_THIS_BUTTON),
    onClick = onClick,
  ) {
    AnimatedContent(isLoading) { loading ->
      if (loading) {
        CircularProgressIndicator(modifier = Modifier.size(MaterialTheme.dimensions.keyline_24))
      } else {
        if (accountRating != null) {
          YourRatingText(modifier, accountRating)
        } else {
          Text(
            text = stringResource(Res.string.core_ui_add_rating),
            style = MaterialTheme.typography.titleSmall,
          )
        }
      }
    }
  }
}
