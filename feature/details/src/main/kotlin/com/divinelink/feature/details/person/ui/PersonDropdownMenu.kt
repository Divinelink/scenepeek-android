package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.shareUrl
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.dropdownmenu.ShareMenuItem
import com.divinelink.core.ui.composition.LocalIntentManager

@Composable
fun PersonDropdownMenu(
  person: Person,
  expanded: Boolean,
  onDismissDropdown: () -> Unit,
) {
  var showShareDialog by remember { mutableStateOf(false) }

  if (showShareDialog) {
    LocalIntentManager.current.shareText(person.shareUrl())
    showShareDialog = false
  }

  DropdownMenu(
    modifier = Modifier
      .widthIn(min = 180.dp)
      .testTag(TestTags.Menu.DROPDOWN_MENU),
    expanded = expanded,
    onDismissRequest = onDismissDropdown,
  ) {
    ShareMenuItem(
      onClick = {
        onDismissDropdown()
        showShareDialog = true
      },
    )
  }
}

@Previews
@Composable
private fun PersonDropdownMenuPreview() {
  AppTheme {
    Surface {
      PersonDropdownMenu(
        Person(
          id = 4495,
          name = "Steve Carell",
          profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
          gender = Gender.MALE,
          role = listOf(PersonRole.Unknown),
          knownForDepartment = "Acting",
        ),
        expanded = true,
        onDismissDropdown = {},
      )
    }
  }
}
