package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.release.CountryRelease
import com.divinelink.core.ui.extension.localizeIsoDate
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_information_release_dates
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReleaseInformation(info: List<CountryRelease>) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      text = stringResource(Res.string.feature_details_information_release_dates),
      style = MaterialTheme.typography.bodyMedium,
    )

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_16),
    ) {
      items(info) { item ->
        Card {
          Column(
            modifier = Modifier.padding(
              horizontal = MaterialTheme.dimensions.keyline_16,
              vertical = MaterialTheme.dimensions.keyline_8,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_2),
          ) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              Text(
                text = stringResource(item.releaseDetail.type.resource) +
                  " ${item.country.flag}",
                style = MaterialTheme.typography.titleSmall,
              )
            }

            item.releaseDetail.releaseDate.localizeIsoDate(true)?.let { date ->
              Text(
                text = date,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
              )
            }
          }
        }
      }
    }
  }
}
