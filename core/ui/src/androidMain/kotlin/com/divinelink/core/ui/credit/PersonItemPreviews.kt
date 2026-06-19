package com.divinelink.core.ui.credit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.details.person.PersonFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider

@Preview(name = "api 30", uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 30)
@Preview(name = "api 30 dark", uiMode = Configuration.UI_MODE_NIGHT_YES, apiLevel = 30)
@Previews
@Composable
fun PersonItemPreview() {
  PreviewLocalProvider {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
      PersonItem(
        person = PersonFactory.SeriesActor.angelaKinsey.copy(
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
        person = PersonFactory.SeriesActor.brianBaumgartner.copy(
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
