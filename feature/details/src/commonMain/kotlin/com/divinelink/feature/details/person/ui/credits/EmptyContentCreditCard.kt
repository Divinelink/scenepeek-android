package com.divinelink.feature.details.person.ui.credits

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.provider.MediaTypeParameterProvider
import com.divinelink.feature.details.Res
import com.divinelink.feature.details.feature_details_no_movies_available
import com.divinelink.feature.details.feature_details_no_movies_available_desc
import com.divinelink.feature.details.feature_details_no_tv_shows_available
import com.divinelink.feature.details.feature_details_no_tv_shows_available_desc
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun EmptyContentCreditCard(
  modifier: Modifier = Modifier,
  type: MediaType,
  name: String,
) {
  if (type == MediaType.UNKNOWN || type == MediaType.PERSON) return

  Card(
    modifier = modifier
      .testTag(TestTags.Person.EMPTY_CONTENT_CREDIT_CARD.format(type.name))
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
        imageVector = when (type) {
          MediaType.MOVIE -> Icons.Default.Movie
          else -> Icons.Default.Tv
        },
        contentDescription = if (type == MediaType.MOVIE) "Movie icon" else "TV Show icon",
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_48),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

      Text(
        text = when (type) {
          MediaType.MOVIE -> stringResource(Res.string.feature_details_no_movies_available)
          else -> stringResource(Res.string.feature_details_no_tv_shows_available)
        },
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_8))

      Text(
        text = when (type) {
          MediaType.MOVIE -> stringResource(
            Res.string.feature_details_no_movies_available_desc,
            name,
          )
          else -> stringResource(Res.string.feature_details_no_tv_shows_available_desc, name)
        },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Previews
@Composable
fun EmptyContentCreditCardPreview(
  @PreviewParameter(MediaTypeParameterProvider::class) type: MediaType,
) {
  AppTheme {
    Surface {
      EmptyContentCreditCard(type = type, name = "Steve Carell")
    }
  }
}
