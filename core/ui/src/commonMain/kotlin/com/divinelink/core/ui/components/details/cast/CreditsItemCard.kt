package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.blurEffect
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.credit.shouldHideEpisodeCount
import com.divinelink.core.ui.provider.PersonParameterProvider
import com.divinelink.core.ui.resources.core_ui_episode_count
import com.divinelink.core.ui.resources.core_ui_ic_female_person_placeholder
import com.divinelink.core.ui.resources.core_ui_ic_person_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun CreditsItemCard(
  modifier: Modifier = Modifier,
  person: Person,
  onPersonClick: (Person) -> Unit,
  obfuscateEpisodes: Boolean = false,
) {
  Card(
    shape = MaterialTheme.shape.medium,
    modifier = modifier
      .clip(MaterialTheme.shape.medium)
      .clipToBounds()
      .widthIn(max = 120.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    onClick = { onPersonClick(person) },
  ) {
    MovieImage(
      path = person.profilePath,
      errorPlaceHolder = if (person.gender == Gender.FEMALE) {
        painterResource(UiDrawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(UiDrawable.core_ui_ic_person_placeholder)
      },
    )

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, bottom = 4.dp, end = 8.dp)
        .height(40.dp),
      text = person.name,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurface,
    )

    person.role.firstOrNull()?.let { role ->
      role.title?.let { title ->
        Column {
          Text(
            modifier = Modifier
              .padding(horizontal = MaterialTheme.dimensions.keyline_8)
              .padding(bottom = MaterialTheme.dimensions.keyline_4),
            text = title,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
          )

          if (role is PersonRole.SeriesActor) {
            val episodes = person.role
              .filterIsInstance<PersonRole.SeriesActor>()
              .sumOf { it.totalEpisodes ?: 0 }

            Text(
              modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensions.keyline_8)
                .padding(bottom = MaterialTheme.dimensions.keyline_4)
                .conditional(
                  condition = obfuscateEpisodes,
                  ifTrue = { blurEffect() },
                ),
              text = if (shouldHideEpisodeCount() && obfuscateEpisodes) {
                ""
              } else {
                pluralStringResource(
                  UiPlurals.core_ui_episode_count,
                  episodes,
                  episodes,
                )
              },
              maxLines = 1,
              style = MaterialTheme.typography.bodySmall,
              overflow = TextOverflow.Ellipsis,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
            )
          }
        }
      }
    }
  }
}

@Previews
@Composable
fun CreditsItemCardPreview(@PreviewParameter(PersonParameterProvider::class) person: Person) {
  AppTheme {
    Surface {
      Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        CreditsItemCard(
          person = person,
          onPersonClick = {},
          obfuscateEpisodes = false,
        )

        CreditsItemCard(
          person = person,
          onPersonClick = {},
          obfuscateEpisodes = true,
        )
      }
    }
  }
}
