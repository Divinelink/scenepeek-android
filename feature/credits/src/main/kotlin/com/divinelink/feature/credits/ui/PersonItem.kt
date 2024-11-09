package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.feature.credits.R
import com.divinelink.core.ui.R as uiR

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
        .wrapContentSize()
        .fillMaxWidth(),
    ) {
      MovieImage(
        path = person.profilePath,
        modifier = Modifier.height(120.dp),
        errorPlaceHolder = if (person.gender == Gender.FEMALE) {
          painterResource(id = uiR.drawable.core_ui_ic_female_person_placeholder)
        } else {
          painterResource(id = uiR.drawable.core_ui_ic_person_placeholder)
        },
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
        // Find the first role of the person, it'll the same for all the roles
        when (person.role.first()) {
          is PersonRole.Crew -> Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            val jobText = buildPersonSubHeader(person.role)

            Text(
              modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
              text = jobText,
            )
          }
          is PersonRole.SeriesActor -> Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            val characterText = buildPersonSubHeader(person.role)

            Text(
              modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
              text = characterText,
            )
          }
          is PersonRole.MovieActor,
          PersonRole.Unknown,
          PersonRole.Director,
          PersonRole.Creator,
          -> {
            // Do nothing
          }
        }
        Spacer(Modifier.weight(1f))
      }
    }
  }
}

/**
 * Builds the text for the character and total episodes.
 * @param roles The role and total episodes.
 * Can be a character if the person is an actor or a job if the person is a crew member.
 */
@Composable
private fun buildPersonSubHeader(roles: List<PersonRole>): AnnotatedString = buildAnnotatedString {
  val baseStyle = MaterialTheme.typography.labelMedium.toSpanStyle()
  val episodeStyle = baseStyle.copy(
    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
  )

  roles.forEachIndexed { index, role ->
    val isLastRole = index == roles.lastIndex

    withStyle(baseStyle) {
      append(
        when (role) {
          is PersonRole.SeriesActor -> role.character.ifBlank { "—" }
          is PersonRole.Crew -> if (role.job.isNullOrBlank()) "—" else role.job
          else -> ""
        },
      )
    }
    // Append episode count if available
    when (role) {
      is PersonRole.SeriesActor -> role.totalEpisodes
      is PersonRole.Crew -> role.totalEpisodes
      else -> null
    }?.let { episodes ->
      append(" ")
      withStyle(episodeStyle) {
        append(
          stringResource(
            R.string.feature_credits_character_total_episodes,
            episodes,
          ),
        )
      }
    }

    // Add separator if not the last item
    if (!isLastRole) {
      append(", ")
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
          knownForDepartment = "Acting",
          role = listOf(
            PersonRole.SeriesActor(
              character = "Character 1",
              totalEpisodes = 10,
            ),
            PersonRole.SeriesActor(
              character = "Character 2",
              totalEpisodes = 5,
            ),
            PersonRole.SeriesActor(
              character = "Character 3",
              totalEpisodes = 5,
            ),
          ),
        ),
        onClick = {},
      )
    }
  }
}
