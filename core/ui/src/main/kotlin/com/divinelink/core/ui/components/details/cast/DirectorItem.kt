package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.R

@Composable
fun DirectorItem(
  director: Person,
  onClick: (Person) -> Unit,
) {
  Column {
    Text(
      modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_12),
      text = stringResource(id = R.string.details__director_title),
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
    )

    TextButton(onClick = { onClick(director) }) {
      Text(
        text = director.name,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
      )
    }
  }
}
