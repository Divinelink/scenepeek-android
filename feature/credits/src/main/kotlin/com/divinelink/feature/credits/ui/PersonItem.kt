package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.feature.credits.R

@Composable
fun PersonItem(
  modifier: Modifier = Modifier,
  person: Person,
  onClick: (Person) -> Unit,
) {
  Card(
    modifier = modifier,
    onClick = { onClick(person) },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Row(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_8)
        .heightIn(max = 120.dp)
        .wrapContentSize()
        .fillMaxWidth(),
    ) {
      MovieImage(
        path = person.profilePath,
        errorPlaceHolder = painterResource(
          id = com.divinelink.core.ui.R.drawable.core_ui_ic_person_placeholder,
        ),
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .padding(start = MaterialTheme.dimensions.keyline_12),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        Spacer(Modifier.weight(1f))
        Text(
          text = person.name,
          style = MaterialTheme.typography.titleMedium,
        )
        when (person.role) {
          PersonRole.Creator -> TODO()
          is PersonRole.Crew -> {
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4)) {
              Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
                text = (person.role as PersonRole.Crew).job!!,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
              )

              Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
                text = stringResource(
                  R.string.feature_credits_character_total_episodes,
                  (person.role as PersonRole.Crew).totalEpisodes!!,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
              )
            }
          }
          PersonRole.Director -> TODO()
          is PersonRole.MovieActor -> TODO()
          is PersonRole.SeriesActor -> {
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4)) {
              Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
                text = (person.role as PersonRole.SeriesActor).character!!,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
              )

              Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
                text = stringResource(
                  R.string.feature_credits_character_total_episodes,
                  (person.role as PersonRole.SeriesActor).totalEpisodes!!,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
              )
            }
          }
          PersonRole.Unknown -> TODO()
        }
        Spacer(Modifier.weight(1f))
      }
    }
  }
}

@Previews
@Composable
private fun PersonItemPreview() {
  AppTheme {
    Surface {
      PersonItem(
        person = Person(
          id = 1,
          name = "Person 1",
          profilePath = "https://image.tmdb.org/t/p/w185/1.jpg",
          role = PersonRole.SeriesActor(
            character = "Character 1",
            totalEpisodes = 10,
          ),
        ),
        onClick = {},
      )
    }
  }
}
