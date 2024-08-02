package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

// TODO Add UI Tests
@Composable
fun CreatorsItem(
  creators: List<Person>?,
  onClick: (Person) -> Unit,
) {
  if (creators.isNullOrEmpty()) return

  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_0),
  ) {
    val creatorSectionTitle = if (creators.size == 1) {
      stringResource(id = R.string.core_ui_series_creator)
    } else {
      stringResource(id = R.string.core_ui_series_creators)
    }

    Text(
      modifier = Modifier.padding(
        start = MaterialTheme.dimensions.keyline_12,
      ),
      text = creatorSectionTitle,
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
    )

    LazyVerticalGrid(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 56.dp, max = 120.dp),
      columns = GridCells.Adaptive(100.dp),
    ) {
      items(creators, key = { it.id }) { creator ->
        TextButton(onClick = { onClick(creator) }) {
          Text(
            text = creator.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
          )
        }
      }
    }
  }
}

@Previews
@Composable
private fun CreatorsItemPreview() {
  AppTheme {
    Surface {
      CreatorsItem(
        creators = listOf(
          Person(
            id = 1216630,
            name = "Greg Daniels",
            profilePath = "/2Hi7Tw0fyYFOZex8BuGsHS8Q4KD.jpg",
            role = PersonRole.Creator,
          ),
          Person(
            id = 17835,
            name = "Ricky Gervais",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            role = PersonRole.Creator,
          ),
          Person(
            id = 123,
            name = "Stephen Merchant",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            role = PersonRole.Creator,
          ),
          Person(
            id = 345,
            name = "Ricky Gervais",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            role = PersonRole.Creator,
          ),
        ),
        onClick = {},
      )
    }
  }
}
