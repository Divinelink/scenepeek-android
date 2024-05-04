package com.andreolas.movierama.ui.components.details.genres

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.ui.theme.AppTheme

@Composable
fun GenreLabel(
  modifier: Modifier = Modifier,
  genre: String,
  onGenreClicked: () -> Unit,
) {
  Surface(
    shape = RoundedCornerShape(4.dp),
    modifier = modifier
      .wrapContentSize(Alignment.Center)
      .wrapContentHeight()
      .clip(RoundedCornerShape(4.dp))
      .clickable {
        onGenreClicked()
      },
    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
  ) {
    Box {
      Text(
        modifier = Modifier
          .align(Alignment.Center)
          .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        textAlign = TextAlign.Center,
        text = genre,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}

@ExcludeFromKoverReport
@Preview(
  name = "Night Mode",
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
  name = "Day Mode",
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Suppress("UnusedPrivateMember")
@Composable
private fun GenreLabelPreview() {
  AppTheme {
    Surface {
      GenreLabel(
        genre = "Documentary",
        onGenreClicked = {},
      )
    }
  }
}
