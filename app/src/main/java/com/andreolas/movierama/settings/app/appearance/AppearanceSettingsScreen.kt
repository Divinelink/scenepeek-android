package com.andreolas.movierama.settings.app.appearance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.components.SettingsRadioPrefItem
import com.andreolas.movierama.settings.components.SettingsScaffold
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun AppearanceSettingsScreen(
  navigator: DestinationsNavigator,
  viewModel: AppearanceSettingsViewModel = hiltViewModel()
) {
  val viewState by viewModel.uiState.collectAsState()

  val resources = LocalContext.current.resources
  val themeLabels = resources.getStringArray(R.array.pref_theme_entries)

  SettingsScaffold(
    title = stringResource(id = R.string.preferences__appearance),
    onNavigationClick = navigator::navigateUp
  ) {

    LazyColumn(contentPadding = it) {
      item {
        SettingsRadioPrefItem(
          title = stringResource(id = R.string.preferences__theme),
          selected = themeLabels[viewState.theme.ordinal],
          selectedIndex = themeLabels.indexOf(viewState.theme.name),
          listItems = themeLabels.toList(),
          onSelected = { index ->
            viewModel.setTheme(viewState.availableThemes[index])
          }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSelectOption(
  title: String,
  listItems: List<String>,
  selectedOption: Int,
  onSelected: (Int) -> Unit,
  onDismissRequest: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    content = {
      Column {
        Text(
          modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_16),
          text = title,
          style = MaterialTheme.typography.headlineSmall,
        )

        listItems.forEachIndexed { index, option ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onSelected(index)
              }
          ) {
            RadioButton(
              selected = selectedOption == index,
              onClick = { onSelected(index) }
            )
            Text(
              text = option,
              modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8)
            )
          }
        }
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun RadioSelectAlertDialog() {
  AppTheme {
    AlertDialogSelectOption(
      title = "Select an Option",
      listItems = listOf("Option 1", "Option 2", "Option 3"),
      selectedOption = 0,
      onSelected = {},
      onDismissRequest = {}
    )
  }
}
