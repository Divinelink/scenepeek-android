package com.divinelink.feature.details.person.ui.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.getString
import com.divinelink.feature.details.R
import com.divinelink.core.ui.R as uiR

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterial3Api::class,
)
@Composable
fun CreditsFilterModalBottomSheet(
  modifier: Modifier = Modifier,
  sheetState: SheetState,
  appliedFilters: List<CreditFilter>,
  filters: List<CreditFilter>,
  onClick: (CreditFilter) -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    modifier = modifier.testTag(TestTags.Person.CREDITS_FILTER_BOTTOM_SHEET),
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    CreditsFilterBottomSheetContent(filters, appliedFilters, onClick)
  }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CreditsFilterBottomSheetContent(
  filters: List<CreditFilter>,
  appliedFilters: List<CreditFilter>,
  onClick: (CreditFilter) -> Unit,
) {
  Column {
    Text(
      text = stringResource(uiR.string.core_ui_filter),
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
    )

    Spacer(Modifier.padding(MaterialTheme.dimensions.keyline_8))
    HorizontalDivider()
    Spacer(Modifier.padding(MaterialTheme.dimensions.keyline_8))
    Text(
      text = stringResource(R.string.feature_details_person_credits_department),
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          vertical = MaterialTheme.dimensions.keyline_8,
          horizontal = MaterialTheme.dimensions.keyline_16,
        ),
      style = MaterialTheme.typography.titleLarge,
    )

    filters.forEach { filter ->
      when (filter) {
        is CreditFilter.Department -> {
          FilterItem(
            filter = filter,
            isApplied = appliedFilters.contains(filter),
            onClick = { onClick(filter) },
          )
        }
        is CreditFilter.SortReleaseDate -> {
          FilterItem(
            filter = filter,
            isApplied = appliedFilters.contains(filter),
            onClick = { onClick(filter) },
          )
        }
      }
      Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
    }
  }
}

@Composable
private fun FilterItem(
  filter: CreditFilter,
  isApplied: Boolean,
  onClick: (CreditFilter) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(filter) },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = filter.title.getString(),
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(
          horizontal = MaterialTheme.dimensions.keyline_16,
          vertical = MaterialTheme.dimensions.keyline_12,
        ),
    )
    if (isApplied) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(end = MaterialTheme.dimensions.keyline_16),
      )
    }
  }
}

@Composable
@Previews
fun CreditsFilterBottomSheetContentPreview() {
  AppTheme {
    Surface {
      CreditsFilterBottomSheetContent(
        filters = listOf(
          CreditFilter.Department("Acting", 10),
          CreditFilter.Department("Directing", 5),
          CreditFilter.SortReleaseDate,
        ),
        appliedFilters = listOf(CreditFilter.Department("Acting", 10)),
        onClick = {},
      )
    }
  }
}
