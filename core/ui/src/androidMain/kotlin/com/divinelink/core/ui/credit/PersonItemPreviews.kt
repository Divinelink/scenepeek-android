package com.divinelink.core.ui.credit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews

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
                creditId = "credit_1",
                order = 1,
              ),
              PersonRole.SeriesActor(
                character = "Character 2",
                totalEpisodes = 5,
                creditId = "credit_2",
                order = 2,
              ),
              PersonRole.SeriesActor(
                character = "Character 3",
                totalEpisodes = 5,
                creditId = "credit_3",
                order = 3,
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
                creditId = "credit_1",
                order = 1,
              ),
              PersonRole.SeriesActor(
                character = "Character 2",
                totalEpisodes = 5,
                creditId = "credit_2",
                order = 2,
              ),
              PersonRole.SeriesActor(
                character = "Character 3",
                totalEpisodes = 5,
                creditId = "credit_3",
                order = 3,
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
