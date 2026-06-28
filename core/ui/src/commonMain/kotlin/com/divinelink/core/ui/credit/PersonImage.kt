package com.divinelink.core.ui.credit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.media.WinnerPill
import com.divinelink.core.ui.resources.core_ui_ic_female_person_placeholder
import com.divinelink.core.ui.resources.core_ui_ic_person_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun PersonImage(
  person: MediaItem.Person,
  isWinner: Boolean = false,
  isSmall: Boolean = false,
  modifier: Modifier = Modifier,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .wrapContentHeight(),
  ) {
    MovieImage(
      modifier = Modifier.height(
        if (isSmall) {
          MaterialTheme.dimensions.posterSizeSmall
        } else {
          MaterialTheme.dimensions.posterSize
        },
      ),
      errorPlaceHolder = if (person.gender == Gender.FEMALE) {
        painterResource(UiDrawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(UiDrawable.core_ui_ic_person_placeholder)
      },
      path = person.posterPath,
    )

    Column(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(
          end = MaterialTheme.dimensions.keyline_4,
          top = MaterialTheme.dimensions.keyline_4,
        ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      horizontalAlignment = Alignment.End,
    ) {
      if (isWinner) {
        WinnerPill()
      }
    }
  }
}
