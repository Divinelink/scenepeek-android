package com.divinelink.core.ui.menu

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.ScreenType
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.LocalIntentManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DropdownMenuButton(
  screenType: ScreenType,
  viewModel: DropdownMenuViewModel = koinViewModel { parametersOf(screenType) },
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var showDropdownMenu by remember { mutableStateOf(false) }
  val intentManager = LocalIntentManager.current

  LaunchedEffect(Unit) {
    viewModel.shareUrl.collectLatest { url ->
      intentManager.shareText(url)
      showDropdownMenu = false
    }
  }

  LaunchedEffect(screenType) {
    viewModel.updateEntryPoint(screenType)
  }

  IconButton(
    modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
    onClick = { showDropdownMenu = !showDropdownMenu },
  ) {
    Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "Show actions")
  }

  if (showDropdownMenu && uiState.availableIntents.isNotEmpty()) {
    DropdownMenu(
      modifier = Modifier
        .widthIn(min = 180.dp)
        .testTag(TestTags.Menu.DROPDOWN_MENU),
      expanded = showDropdownMenu,
      onDismissRequest = { showDropdownMenu = false },
    ) {
      uiState.availableIntents.forEach { intent ->
        ScenePeekMenuItem(
          intent = intent,
          onClick = { viewModel.onAction(intent) },
        )
      }
    }
  }
}
