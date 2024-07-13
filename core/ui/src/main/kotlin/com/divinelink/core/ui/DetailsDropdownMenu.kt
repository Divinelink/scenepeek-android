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
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.shareUrl
import com.divinelink.core.ui.components.dialog.RequestMovieDialog
import com.divinelink.core.ui.components.dialog.SelectSeasonsDialog

@Composable
fun DetailsDropdownMenu(
  mediaDetails: MediaDetails,
  menuOptions: List<DetailsMenuOptions>,
  expanded: Boolean,
  requestMedia: (List<Int>) -> Unit,
  onDismissDropdown: () -> Unit,
) {
  var showShareDialog by remember { mutableStateOf(false) }
  var showRequestDialog by remember { mutableStateOf(false) }

  if (showShareDialog) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, mediaDetails.shareUrl())
    }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share via"))
    showShareDialog = false
  }

  if (showRequestDialog) {
    when (mediaDetails) {
      is TV -> SelectSeasonsDialog(
        numberOfSeasons = mediaDetails.numberOfSeasons,
        onRequestClick = {
          requestMedia(it)
          showRequestDialog = false
        },
        onDismissRequest = { showRequestDialog = false },
      )
      is Movie -> RequestMovieDialog(
        onDismissRequest = { showRequestDialog = false },
        onConfirm = {
          requestMedia(emptyList())
          showRequestDialog = false
        },
        title = mediaDetails.title,
      )
    }
  }

  DropdownMenu(
    modifier = Modifier
      .widthIn(min = 180.dp)
      .testTag(TestTags.Menu.DROPDOWN_MENU),
    expanded = expanded,
    onDismissRequest = onDismissDropdown,
  ) {
    DropdownMenuItem(
      modifier = Modifier.testTag(
        TestTags.Menu.MENU_ITEM.format(stringResource(id = R.string.core_ui_share)),
      ),
      text = { Text(text = stringResource(id = R.string.core_ui_share)) },
      onClick = {
        onDismissDropdown()
        showShareDialog = true
      },
    )

    if (menuOptions.contains(DetailsMenuOptions.REQUEST)) {
      DropdownMenuItem(
        modifier = Modifier.testTag(
          TestTags.Menu.MENU_ITEM.format(stringResource(R.string.core_ui_dropdown_menu_request)),
        ),
        leadingIcon = {
          Image(
            painter = painterResource(id = R.drawable.core_ui_ic_jellyseerr),
            contentDescription = null,
          )
        },
        text = { Text(text = stringResource(id = R.string.core_ui_dropdown_menu_request)) },
        onClick = {
          onDismissDropdown()
          showRequestDialog = true
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
          director = Person(
            id = 123443321,
            name = "Forest Gump",
            profilePath = "BoxOfChocolates.jpg",
            role = PersonRole.Director,
          ),
          cast = listOf(),
          genres = listOf("Thriller", "Drama", "Comedy"),
          runtime = "2h 10m",
        ),
        menuOptions = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.REQUEST),
        requestMedia = {},
        expanded = true,
        onDismissDropdown = {},
      )
    }
  }
}
