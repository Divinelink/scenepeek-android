package com.divinelink.core.ui.credit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.resources.core_ui_ic_female_person_placeholder
import com.divinelink.core.ui.resources.core_ui_ic_person_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun SmallPersonItem(
  modifier: Modifier = Modifier,
  person: Person,
  onClick: (Person) -> Unit,
) {
  Card(
    modifier = modifier,
    onClick = { onClick(person) },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Column(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_8)
        .wrapContentSize()
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      MovieImage(
        path = person.profilePath,
        modifier = Modifier.height(MaterialTheme.dimensions.shortMediaCard),
        errorPlaceHolder = if (person.gender == Gender.FEMALE) {
          painterResource(UiDrawable.core_ui_ic_female_person_placeholder)
        } else {
          painterResource(UiDrawable.core_ui_ic_person_placeholder)
        },
      )

      Text(
        text = person.name,
        style = MaterialTheme.typography.bodySmall,
      )

      when (person.role.first()) {
        is PersonRole.Crew -> Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            text = buildString {
              append(
                person.role
                  .filter { it.title != null }
                  .joinToString(", ") { it.title ?: "" },
              )
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        is PersonRole.SeriesActor -> Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            text = buildString {
              append(
                person.role
                  .filter { it.title != null }
                  .joinToString(", ") { it.title ?: "" },
              )
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        is PersonRole.MovieActor -> Text(
          text = buildString {
            append(
              person.role
                .filter { it.title != null }
                .joinToString(", ") { it.title ?: "" },
            )
          },
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PersonRole.Unknown,
        PersonRole.Director,
        PersonRole.Creator,
        PersonRole.Novel,
        PersonRole.Screenplay,
          -> Unit
      }
    }
  }
}
