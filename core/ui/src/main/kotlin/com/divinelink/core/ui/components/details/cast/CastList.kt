package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@Composable
fun CastList(
  cast: List<Person>,
  onPersonClick: (Person) -> Unit,
  onViewAllClick: () -> Unit,
  viewAllVisible: Boolean = true,
) {
  Column(
    modifier = Modifier
      .padding(top = MaterialTheme.dimensions.keyline_4)
      .fillMaxWidth(),
  ) {
    if (cast.isNotEmpty()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_12),
        horizontalArrangement = Arrangement.Center,
      ) {
        Text(
          modifier = Modifier.align(alignment = Alignment.CenterVertically),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          text = stringResource(id = R.string.details__cast_title),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (viewAllVisible) {
          TextButton(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            onClick = onViewAllClick,
          ) {
            Text(stringResource(id = R.string.core_ui_view_all))
          }
        } else {
          Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_40))
        }
      }

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        contentPadding = PaddingValues(
          top = MaterialTheme.dimensions.keyline_4,
          start = MaterialTheme.dimensions.keyline_12,
          end = MaterialTheme.dimensions.keyline_12,
          bottom = MaterialTheme.dimensions.keyline_12,
        ),
      ) {
        items(
          items = cast,
          key = { it.id },
        ) {
          CreditsItemCard(person = it, onPersonClick = onPersonClick)
        }
      }
    }
  }
}

@Previews
@Composable
private fun CastListPreview() {
  AppTheme {
    Surface {
      CastList(
        cast = listOf(
          Person(
            id = 1,
            name = "John Doe",
            profilePath = null,
            knownForDepartment = "Acting",
            role = listOf(
              PersonRole.SeriesActor(
                character = "Character Name",
                totalEpisodes = 10,
              ),
            ),
          ),
          Person(
            id = 2,
            name = "Jane Doe",
            profilePath = "/profile.jpg",
            knownForDepartment = "Acting",
            role = listOf(
              PersonRole.SeriesActor(
                character = "Character Name",
                totalEpisodes = 10,
              ),
              PersonRole.SeriesActor(
                character = "Character Name 2",
                totalEpisodes = 5,
              ),
            ),
          ),
        ),
        onPersonClick = {},
        onViewAllClick = {},
      )
    }
  }
}
