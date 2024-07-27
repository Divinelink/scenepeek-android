package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.PopularMovieItemShape
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.R

@Composable
fun CreditsItemCard(
  modifier: Modifier = Modifier,
  person: Person,
  onPersonClick: (Person) -> Unit,
) {
  Card(
    shape = PopularMovieItemShape,
    modifier = modifier
      .clip(PopularMovieItemShape)
      .clipToBounds()
      .widthIn(max = 120.dp)
      .clickable {
        // todo
      },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    onClick = { onPersonClick(person) },
  ) {
    MovieImage(
      modifier = Modifier,
      path = person.profilePath,
      errorPlaceHolder = painterResource(id = R.drawable.core_ui_ic_person_placeholder),
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

    Column {
      person.role.title?.let { character ->
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.dimensions.keyline_8)
            .padding(bottom = MaterialTheme.dimensions.keyline_4),
          text = character,
          maxLines = 1,
          style = MaterialTheme.typography.bodySmall,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }
      if (person.role is PersonRole.SeriesActor) {
        val role = person.role as PersonRole.SeriesActor
        role.totalEpisodes?.let { episodes ->
          Text(
            modifier = Modifier
              .padding(horizontal = MaterialTheme.dimensions.keyline_8)
              .padding(bottom = MaterialTheme.dimensions.keyline_4),
            text = "$episodes episodes",
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

@Preview
@Composable
private fun CreditsItemCardPreview() {
  Surface {
    AppTheme {
      CreditsItemCard(
        person = Person(
          id = 94622,
          name = "Brian Baumgartner",
          role = PersonRole.SeriesActor(
            "Kevin Malone",
            totalEpisodes = 217,
          ),
          profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
        ),
        onPersonClick = {},
      )
    }
  }
}
