package com.divinelink.feature.details.media.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.provider.WatchProviders
import com.divinelink.core.model.locale.Country

@Composable
fun WatchProvidersContent(
  watchProviders: WatchProviders,
) {
  val forRegion = remember { watchProviders.results[Country.UNITED_STATES] }

  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = "Available on",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.tertiary,
      )

      Spacer(modifier = Modifier.weight(1f))

      Text(
        text = "by JustWatch",
        style = MaterialTheme.typography.bodyMedium,
      )
    }

    if (forRegion == null || forRegion.isEmpty) {
      Text(
        modifier = Modifier
          .padding(top = MaterialTheme.dimensions.keyline_8)
          .padding(horizontal = MaterialTheme.dimensions.keyline_16),
        text = "No streaming providers available for this region.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
      )
    } else {
      LazyRow(
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_16),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        items(
          items = watchProviders.results[Country.UNITED_STATES]?.stream ?: emptyList(),
          key = { it.displayPriority },
        ) {
          StreamingProviderItem(it, subtitle = "Stream")
        }
        items(
          items = watchProviders.results[Country.UNITED_STATES]?.buy ?: emptyList(),
          key = { it.displayPriority },
        ) {
          StreamingProviderItem(it, subtitle = "Buy")
        }
        items(
          items = watchProviders.results[Country.UNITED_STATES]?.rent ?: emptyList(),
          key = { it.displayPriority },
        ) {
          StreamingProviderItem(it, subtitle = "Rent")
        }
      }
    }
  }
}
