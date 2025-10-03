package com.divinelink.core.ui.components.details.genres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.fixtures.model.GenreFactory
import com.divinelink.core.model.Genre
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider

@Composable
fun GenreLabel(
  modifier: Modifier = Modifier,
  genre: Genre,
  onGenreClick: (Genre) -> Unit,
) {
  Surface(
    shape = RoundedCornerShape(4.dp),
    modifier = modifier
      .wrapContentSize(Alignment.Center)
      .wrapContentHeight()
      .clip(RoundedCornerShape(4.dp)),
//      .clickable {
//        onGenreClick(genre)
//      },
    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
  ) {
    Box {
      Text(
        modifier = Modifier
          .align(Alignment.Center)
          .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        textAlign = TextAlign.Center,
        text = genre.name,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}

@Previews
@Composable
private fun GenreLabelPreview() {
  PreviewLocalProvider {
    GenreLabel(
      genre = GenreFactory.Movie.documentary,
      onGenreClick = {},
    )
  }
}
