package com.divinelink.feature.discover.filters.year

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.Decade

@Composable
fun YearPickerDialog(
  onYearSelected: (Int) -> Unit,
  onDismiss: () -> Unit,
  selectedYear: Int,
  yearRange: IntRange = IntRange(
    start = Decade.DECADE_1900.startYear,
    endInclusive = Decade.entries.last().endYear,
  ),
  selectableYears: List<Int> = IntRange(
    start = Decade.DECADE_1900.startYear,
    endInclusive = Decade.entries.last().endYear,
  ).toList(),
  title: String,
) {
  val state = rememberLazyGridState()

  LaunchedEffect(Unit) {
    state.scrollToItem(selectedYear - yearRange.first)
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .fillMaxHeight(0.8f),
      shape = MaterialTheme.shapes.large,
      color = MaterialTheme.colorScheme.surface,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(MaterialTheme.dimensions.keyline_16),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimensions.keyline_24),
          textAlign = TextAlign.Center,
        )

        LazyVerticalGrid(
          modifier = Modifier.fillMaxWidth(),
          state = state,
          columns = GridCells.Adaptive(minSize = MaterialTheme.dimensions.keyline_78),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          val decades = yearRange.toList().groupBy { year ->
            (year / 10) * 10
          }

          decades.forEach { (decadeStart, decadeYears) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
              DecadeHeader(
                startYear = decadeStart,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                    horizontal = MaterialTheme.dimensions.keyline_8,
                    vertical = MaterialTheme.dimensions.keyline_4,
                  ),
              )
            }

            items(decadeYears) { year ->
              YearButton(
                year = year,
                isSelected = selectedYear == year,
                isSelectable = year in selectableYears,
                onClick = {
                  onYearSelected(year)
                  onDismiss()
                },
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun YearButton(
  year: Int,
  isSelected: Boolean,
  isSelectable: Boolean,
  onClick: () -> Unit,
) {
  TextButton(
    enabled = isSelectable,
    onClick = onClick,
    contentPadding = PaddingValues(
      vertical = MaterialTheme.dimensions.keyline_8,
      horizontal = MaterialTheme.dimensions.keyline_4,
    ),
    colors = if (isSelected) {
      ButtonDefaults.textButtonColors().copy(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      )
    } else {
      ButtonDefaults.textButtonColors()
    },
  ) {
    Text(
      text = year.toString(),
    )
  }
}
