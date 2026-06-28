package com.divinelink.core.ui.credit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.provider.PersonParameterProvider

@Composable
fun SmallPersonItem(
  modifier: Modifier = Modifier,
  person: MediaItem.Person,
  isSmall: Boolean = true,
  isWinner: Boolean = false,
  footerContent: (@Composable () -> Unit)? = null,
  onClick: (MediaItem.Person) -> Unit,
) {
  Card(
    modifier = modifier
      .clip(MaterialTheme.shape.medium)
      .widthIn(max = MaterialTheme.dimensions.shortMediaCard),
    onClick = { onClick(person) },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Column(
      modifier = Modifier
        .wrapContentSize()
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      PersonImage(
        person = person,
        isSmall = isSmall,
        isWinner = isWinner,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))

      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = MaterialTheme.dimensions.keyline_4)
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        text = person.name,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
        textAlign = TextAlign.Center,
      )

      footerContent?.let {
        footerContent()
      }

      when (person.role.first()) {
        is PersonRole.Crew -> Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            modifier = Modifier
              .personRoleModifier(),
            text = buildString {
              append(
                person.role
                  .filter { it.title != null }
                  .joinToString(", ") { it.title ?: "" },
              )
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )
        }
        is PersonRole.SeriesActor -> Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            modifier = Modifier
              .personRoleModifier(),
            text = buildString {
              append(
                person.role
                  .filter { it.title != null }
                  .joinToString(", ") { it.title ?: "" },
              )
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )
        }
        is PersonRole.MovieActor -> Text(
          modifier = Modifier
            .personRoleModifier(),
          text = buildString {
            append(
              person.role
                .filter { it.title != null }
                .joinToString(", ") { it.title ?: "" },
            )
          },
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
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

@Composable
private fun Modifier.personRoleModifier(): Modifier = this
  .padding(horizontal = MaterialTheme.dimensions.keyline_8)
  .padding(bottom = MaterialTheme.dimensions.keyline_8)
  .fillMaxWidth()

@Composable
@Previews
fun SmallPersonItemPreviews(
  @PreviewParameter(PersonParameterProvider::class) person: MediaItem.Person,
) {
  PreviewLocalProvider {
    SmallPersonItem(
      person = person,
      isSmall = true,
      footerContent = {},
      onClick = {},
    )
  }
}
