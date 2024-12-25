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
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.shareUrl
import com.divinelink.core.ui.components.dropdownmenu.ObfuscateSpoilersMenuItem
import com.divinelink.core.ui.components.dropdownmenu.ShareMenuItem

@Composable
fun DetailsDropdownMenu(
  mediaDetails: MediaDetails,
  spoilersObfuscated: Boolean,
  options: List<DetailsMenuOptions>,
  expanded: Boolean,
  onDismissDropdown: () -> Unit,
  onObfuscateClick: () -> Unit,
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
        DetailsMenuOptions.OBFUSCATE_SPOILERS -> ObfuscateSpoilersMenuItem(
          obfuscated = spoilersObfuscated,
          onClick = onObfuscateClick,
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
        mediaDetails = MediaDetailsFactory.FightClub(),
        spoilersObfuscated = false,
        expanded = true,
        options = DetailsMenuOptions.entries,
        onDismissDropdown = {},
        onObfuscateClick = {},
      )
    }
  }
}
