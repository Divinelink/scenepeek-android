package com.divinelink.core.ui.credit

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.conditional

@Composable
fun PersonItem(
  modifier: Modifier = Modifier,
  person: Person,
  onClick: (Person) -> Unit,
  isObfuscated: Boolean,
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
        modifier = Modifier.height(MaterialTheme.dimensions.shortMediaCard),
        errorPlaceHolder = if (person.gender == Gender.FEMALE) {
          painterResource(id = R.drawable.core_ui_ic_female_person_placeholder)
        } else {
          painterResource(id = R.drawable.core_ui_ic_person_placeholder)
        },
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = MaterialTheme.dimensions.keyline_12),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        Text(
          text = person.name,
          style = MaterialTheme.typography.titleMedium,
        )
        // Find the first role of the person, it'll the same for all the roles
        when (person.role.first()) {
          is PersonRole.Crew -> Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            BuildPersonSubHeader(roles = person.role, isObfuscated = isObfuscated)
          }
          is PersonRole.SeriesActor -> Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          ) {
            BuildPersonSubHeader(roles = person.role, isObfuscated = isObfuscated)
          }
          is PersonRole.MovieActor -> {
            Text(
              text = buildString {
                append(
                  person.role
                    .filter { it.title != null }
                    .joinToString(", ") { it.title ?: "" },
                )
              },
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          PersonRole.Unknown,
          PersonRole.Director,
          PersonRole.Creator,
          PersonRole.Novel,
          PersonRole.Screenplay,
          -> {
            // Do nothing
          }
        }
      }
    }
  }
}

/**
 * Builds the text for the character and total episodes.
 * @param roles The role and total episodes.
 * Can be a character if the person is an actor or a job if the person is a crew member.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuildPersonSubHeader(
  roles: List<PersonRole>,
  isObfuscated: Boolean,
) {
  FlowRow(
    modifier = Modifier.fillMaxWidth(),
    overflow = FlowRowOverflow.Visible,
  ) {
    roles.forEachIndexed { index, role ->
      val isLastRole = index == roles.lastIndex

      val name = when (role) {
        is PersonRole.SeriesActor -> role.character
        is PersonRole.Crew -> role.job
        else -> ""
      }

      CharacterWithBlurredEpisodes(
        characterName = name ?: "—",
        episodes = when (role) {
          is PersonRole.SeriesActor -> role.totalEpisodes ?: 0
          is PersonRole.Crew -> role.totalEpisodes?.toInt() ?: 0
          else -> 0
        },
        isObfuscated = isObfuscated,
      )

      if (!isLastRole) {
        Text(text = ", ", style = MaterialTheme.typography.labelMedium)
      }
    }
  }
}

@Composable
fun CharacterWithBlurredEpisodes(
  characterName: String,
  episodes: Int,
  isObfuscated: Boolean,
) {
  val baseStyle = MaterialTheme.typography.labelMedium
  val episodeStyle = baseStyle.copy(
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )

  Text(
    text = characterName,
    style = baseStyle,
  )

  val episodesCount = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S && isObfuscated) {
    ""
  } else {
    Text(
      text = " ",
      style = baseStyle,
    )
    pluralStringResource(
      R.plurals.core_ui_character_episode_episodes,
      episodes,
      episodes,
    )
  }

  Text(
    text = episodesCount,
    style = episodeStyle,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.conditional(
      condition = isObfuscated,
      ifTrue = { blurEffect() },
    ),
  )
}

@Preview(name = "api 30", uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 30)
@Preview(name = "api 30 dark", uiMode = Configuration.UI_MODE_NIGHT_YES, apiLevel = 30)
@Previews
@Composable
fun PersonItemPreview() {
  AppTheme {
    Surface {
      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
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
          isObfuscated = false,
        )

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
          isObfuscated = true,
        )
      }
    }
  }
}
