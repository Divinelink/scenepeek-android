package com.divinelink.feature.discover.filters.year

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.Decade
import com.divinelink.core.model.discover.YearType
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_from
import com.divinelink.core.ui.resources.core_ui_select_end_year
import com.divinelink.core.ui.resources.core_ui_select_start_year
import com.divinelink.core.ui.resources.core_ui_select_year
import com.divinelink.core.ui.resources.core_ui_to
import com.divinelink.core.ui.resources.core_ui_year
import com.divinelink.feature.discover.FilterType
import com.divinelink.feature.discover.filters.SelectFilterAction
import org.jetbrains.compose.resources.stringResource

@Composable
fun YearFilterContent(
  filter: FilterType.Year,
  action: (SelectFilterAction) -> Unit,
) {
  var selectedChip by remember(filter) { mutableStateOf(filter) }
  var showSingleDialog by remember { mutableStateOf(false) }
  var showStartDialog by remember { mutableStateOf(false) }
  var showEndDialog by remember { mutableStateOf(false) }

  if (showSingleDialog) {
    YearPickerDialog(
      onYearSelected = { action(SelectFilterAction.UpdateSingleYear(it)) },
      onDismiss = { showSingleDialog = false },
      selectedYear = (filter as FilterType.Year.Single).year,
      title = stringResource(UiString.core_ui_select_year),
    )
  }

  if (showStartDialog) {
    YearPickerDialog(
      onYearSelected = { action(SelectFilterAction.UpdateStartYear(it)) },
      title = stringResource(UiString.core_ui_select_start_year),
      onDismiss = { showStartDialog = false },
      selectableYears = IntRange(
        start = Decade.DECADE_1900.startYear,
        endInclusive = (filter as? FilterType.Year.Range)?.endYear ?: Decade.DECADE_2020.endYear,
      ).toList(),
      selectedYear = (filter as FilterType.Year.Range).startYear,
    )
  }

  if (showEndDialog) {
    YearPickerDialog(
      onYearSelected = { action(SelectFilterAction.UpdateEndYear(it)) },
      title = stringResource(UiString.core_ui_select_end_year),
      onDismiss = { showEndDialog = false },
      selectableYears = IntRange(
        start = (filter as? FilterType.Year.Range)?.startYear ?: Decade.DECADE_1900.startYear,
        endInclusive = Decade.DECADE_2020.endYear,
      ).toList(),
      selectedYear = (filter as FilterType.Year.Range).endYear,
    )
  }

  LazyColumn(
    modifier = Modifier
      .animateContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(UiString.core_ui_year),
        style = MaterialTheme.typography.titleMedium,
      )
    }

    item {
      YearChips(
        chips = YearType.entries,
        selected = selectedChip.type,
        onClick = { action(SelectFilterAction.UpdateYearType(it)) },
      )
    }

    item {
      when (val filter = selectedChip) {
        FilterType.Year.Any -> Unit
        is FilterType.Year.Range -> Row(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          SelectYearField(
            modifier = Modifier.weight(1f),
            title = stringResource(UiString.core_ui_from),
            year = filter.startYear,
            onClick = { showStartDialog = true },
          )

          SelectYearField(
            modifier = Modifier.weight(1f),
            title = stringResource(UiString.core_ui_to),
            year = filter.endYear,
            onClick = { showEndDialog = true },
          )
        }

        is FilterType.Year.Single -> SelectYearField(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          year = filter.year,
          onClick = { showSingleDialog = true },
        )
        is FilterType.Year.Decade -> FlowRow(
          modifier = Modifier.padding(
            horizontal = MaterialTheme.dimensions.keyline_8,
            vertical = MaterialTheme.dimensions.keyline_16,
          ),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Decade.entries.forEach { entry ->
            TextButton(
              onClick = { action(SelectFilterAction.OnSelectDecade(entry)) },
              contentPadding = PaddingValues(
                vertical = MaterialTheme.dimensions.keyline_8,
                horizontal = MaterialTheme.dimensions.keyline_8,
              ),
              colors = if (filter.decade == entry) {
                ButtonDefaults.textButtonColors().copy(
                  containerColor = MaterialTheme.colorScheme.primaryContainer,
                  contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
              } else {
                ButtonDefaults.textButtonColors()
              },
            ) {
              Text(text = entry.label)
            }
          }
        }
      }
    }
  }
}
