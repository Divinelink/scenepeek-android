package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.commons.extensions.toLocalDate
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.extension.localizeMonthYear
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_number_of_seasons
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleDetails(
  modifier: Modifier = Modifier,
  mediaDetails: MediaDetails,
) {
  Column(modifier = modifier) {
    Text(
      style = MaterialTheme.typography.titleLarge,
      text = mediaDetails.title,
    )

    FlowRow {
      Text(
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        text = mediaDetails.releaseDate.toLocalDate()?.localizeMonthYear(useLong = false)
          ?: mediaDetails.releaseDate,
      )

      when (mediaDetails) {
        is Movie -> mediaDetails.runtime?.let { runtime ->
          Text(
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = " • $runtime",
          )
        }
        is TV -> {
          if (mediaDetails.information.status != TvStatus.UNKNOWN) {
            Text(
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              text = " • " + stringResource(mediaDetails.information.status.resId),
            )
          }

          if (mediaDetails.numberOfSeasons > 0) {
            Text(
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              text = " • " + pluralStringResource(
                Res.plurals.feature_details_number_of_seasons,
                mediaDetails.numberOfSeasons,
                mediaDetails.numberOfSeasons,
              ),
            )
          }
        }
      }
    }
  }
}

@Composable
@Previews
fun TitleDetailsPreview() {
  AppTheme {
    Surface {
      TitleDetails(
        mediaDetails = MediaDetailsFactory.FightClub(),
      )
    }
  }
}

@Composable
@Previews
fun TitleTvDetailsPreview() {
  AppTheme {
    Surface {
      TitleDetails(
        mediaDetails = MediaDetailsFactory.TheOffice(),
      )
    }
  }
}
