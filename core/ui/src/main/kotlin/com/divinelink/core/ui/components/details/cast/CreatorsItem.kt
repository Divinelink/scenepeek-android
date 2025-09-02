package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@Composable
fun CreatorsItem(
  creators: List<Person>,
  onClick: (Person) -> Unit,
) {
  FlowRow(
    modifier = Modifier.offset(x = -MaterialTheme.dimensions.keyline_12),
  ) {
    creators.forEach { person ->
      CreatorItem(
        person = person,
        onClick = onClick,
      )
    }
  }
}

@Composable
private fun CreatorItem(
  person: Person,
  onClick: (Person) -> Unit,
) {
  val director = stringResource(id = R.string.core_ui_director)
  val screenplay = stringResource(id = R.string.core_ui_screenplay)
  val novel = stringResource(id = R.string.core_ui_novel)
  val creator = stringResource(id = R.string.core_ui_creator)

  TextButton(
    onClick = { onClick(person) },
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      Text(
        text = person.name,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
      )

      val roles = person
        .role
        .joinToString {
          when (it) {
            PersonRole.Director -> director
            PersonRole.Screenplay -> screenplay
            PersonRole.Novel -> novel
            PersonRole.Creator -> creator
            else -> ""
          }
        }

      Text(
        text = roles,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Previews
@Composable
private fun TvCreatorsItemPreview() {
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
