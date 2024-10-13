package com.divinelink.core.ui

import android.content.Intent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.shareUrl
import com.divinelink.core.ui.components.dropdownmenu.ShareMenuItem

@Composable
fun DetailsDropdownMenu(
  mediaDetails: MediaDetails,
  options: List<DetailsMenuOptions>,
  expanded: Boolean,
  onDismissDropdown: () -> Unit,
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
    onDismissRequest = onDismissDropdown,
  ) {
    options.forEach { option ->
      when (option) {
        DetailsMenuOptions.SHARE -> ShareMenuItem(
          onClick = {
            onDismissDropdown()
            showShareDialog = true
          },
        )
      }
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
            knownForDepartment = "Acting",
            role = PersonRole.Director,
          ),
          cast = listOf(),
          genres = listOf("Thriller", "Drama", "Comedy"),
          runtime = "2h 10m",
        ),
        expanded = true,
        options = DetailsMenuOptions.entries,
        onDismissDropdown = {},
      )
    }
  }
}
