package com.divinelink.feature.season.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.commons.extensions.toLocalDate
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Season
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.extension.localizeFull

@Composable
fun SeasonTitleDetails(
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  title: String,
  season: Season,
) {
  Column(modifier = modifier) {
    Text(
      modifier = Modifier.clickable { onNavigate(Navigation.Back) },
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.titleSmall,
      text = title,
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_8))

    Text(
      style = MaterialTheme.typography.titleMedium,
      text = season.name,
    )

    Text(
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      text = season.airDate.toLocalDate()?.localizeFull(useLong = true) ?: season.airDate,
    )
  }
}
