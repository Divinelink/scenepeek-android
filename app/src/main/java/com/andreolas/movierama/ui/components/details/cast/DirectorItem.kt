package com.andreolas.movierama.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.R
import com.divinelink.core.model.details.crew.Director

@Composable
fun DirectorItem(
  modifier: Modifier = Modifier,
  director: Director,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = Modifier.padding(
      start = 8.dp, top = 16.dp,
    )
  ) {

    Text(
      text = stringResource(id = R.string.details__director_title),
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
      text = director.name,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
    )
  }
}
