package com.divinelink.core.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.crew.Director
import com.divinelink.core.model.details.shareUrl

@Composable
fun DetailsDropdownMenu(
  mediaDetails: MediaDetails,
  menuOptions: List<DetailsMenuOptions>,
  expanded: Boolean,
  requestMedia: (List<Int>) -> Unit,
  onDismissRequest: () -> Unit,
) {
  var showShareDialog by remember { mutableStateOf(false) }

  if (showShareDialog) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, mediaDetails.shareUrl())
    }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share via"))
    showShareDialog = false
  }

  DropdownMenu(
    modifier = Modifier
      .widthIn(min = 180.dp)
      .testTag(TestTags.Menu.DROPDOWN_MENU),
    expanded = expanded,
    onDismissRequest = onDismissRequest,
  ) {
    DropdownMenuItem(
      modifier = Modifier.testTag(
        TestTags.Menu.MENU_ITEM.format(stringResource(id = R.string.core_ui_share)),
      ),
      text = { Text(text = stringResource(id = R.string.core_ui_share)) },
      onClick = {
        onDismissRequest()
        showShareDialog = true
      },
    )

    if (menuOptions.contains(DetailsMenuOptions.REQUEST)) {
      DropdownMenuItem(
        leadingIcon = {
          Image(
            painter = painterResource(id = R.drawable.core_ui_ic_jellyseerr),
            contentDescription = null,
          )
        },
        text = { Text(text = stringResource(id = R.string.core_ui_dropdown_menu_request)) },
        onClick = {
          onDismissRequest()
          requestMedia(
            listOf(1), // TODO Add dialog
          )
        },
      )
    }
  }
}

@Previews
@Composable
private fun DetailsDropdownMenuPreview() {
  AppTheme {
    Surface {
      DetailsDropdownMenu(
        mediaDetails = Movie(
          id = 1123,
          posterPath = "123456",
          releaseDate = "2022",
          title = "Flight Club",
          rating = "7.3",
          isFavorite = false,
          overview = "This movie is good.",
          director = Director(
            id = 123443321,
            name = "Forest Gump",
            profilePath = "BoxOfChocolates.jpg",
          ),
          cast = listOf(),
          genres = listOf("Thriller", "Drama", "Comedy"),
          runtime = "2h 10m",
        ),
        menuOptions = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.REQUEST),
        requestMedia = {},
        expanded = true,
        onDismissRequest = {},
      )
    }
  }
}
