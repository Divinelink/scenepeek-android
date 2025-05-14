package com.divinelink.feature.details.media.ui.forms.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.feature.details.R

@Composable
fun EmptyRecommendationsCard(
  modifier: Modifier = Modifier,
  name: String,
  isTv: Boolean,
) {
  Card(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth()
      .wrapContentHeight(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ),
  ) {
    Column(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_24)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Icon(
        imageVector = when (isTv) {
          true -> Icons.Default.Tv
          false -> Icons.Default.Movie
        },
        contentDescription = if (isTv) "TV Show icon" else "Movie icon",
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_48),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

      Text(
        text = stringResource(R.string.feature_details_no_recommendation_available),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_8))

      Text(
        text = stringResource(R.string.feature_details_no_recommendation_available, name),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Previews
@Composable
fun EmptyRecommendationCardPreview() {
  AppTheme {
    Surface {
      EmptyRecommendationsCard(isTv = true, name = "The Office")
    }
  }
}
