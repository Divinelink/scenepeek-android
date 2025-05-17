package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatorsItem(
  creators: List<Person>?,
  onClick: (Person) -> Unit,
) {
  if (creators.isNullOrEmpty()) return

  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_0),
  ) {
    Text(
      text = pluralStringResource(
        id = R.plurals.core_ui_series_creator,
        creators.size,
        creators.size,
      ),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurface,
    )

    FlowRow(
      modifier = Modifier.fillMaxWidth(),
    ) {
      creators.forEach {
        TextButton(
          modifier = Modifier.offset(x = -MaterialTheme.dimensions.keyline_12),
          onClick = { onClick(it) },
        ) {
          Text(
            text = it.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            knownForDepartment = "Writing",
            role = listOf(PersonRole.Creator),
          ),
          Person(
            id = 17835,
            name = "Ricky Gervais",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            knownForDepartment = "Writing",
            role = listOf(PersonRole.Creator),
          ),
          Person(
            id = 123,
            name = "Stephen Merchant",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            knownForDepartment = "Writing",
            role = listOf(PersonRole.Creator),
          ),
          Person(
            id = 345,
            name = "Ricky Gervais",
            profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
            knownForDepartment = "Writing",
            role = listOf(PersonRole.Creator),
          ),
        ),
        onClick = {},
      )
    }
  }
}
