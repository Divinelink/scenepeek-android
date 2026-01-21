package com.divinelink.feature.discover.filters

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_country
import com.divinelink.core.ui.resources.core_ui_language
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.FilterType
import com.divinelink.feature.discover.filters.year.YearFilterContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFilterModalBottomSheet(
  type: FilterModal,
  mediaType: MediaType,
  viewModel: SelectFilterViewModel = koinViewModel(
    key = "SelectGenreModalBottomSheet-${mediaType.value}-$type",
  ) { parametersOf(mediaType, type) },
  onDismissRequest: () -> Unit,
) {
  val density = LocalDensity.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier.wrapContentSize(),
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    when (val filterType = uiState.filterType) {
      is FilterType.Searchable.Genres -> SelectGenresContent(
        uiState = uiState,
        action = viewModel::onAction,
        density = density,
        onDismissRequest = onDismissRequest,
      )
      is FilterType.Searchable.Languages -> SelectableFilterList(
        titleRes = UiString.core_ui_language,
        items = filterType.visibleOptions,
        key = { it.code },
        isSelected = { it in filterType.selectedOptions },
        onItemClick = {
          viewModel.onAction(SelectFilterAction.SelectLanguage(it))
          onDismissRequest()
        },
        itemName = { stringResource(it.nameRes) },
        selected = filterType.selectedOptions.firstOrNull(),
        onValueChange = { viewModel.onAction(SelectFilterAction.SearchFilters(it)) },
        query = filterType.query,
      )
      is FilterType.Searchable.Countries -> SelectableFilterList(
        titleRes = UiString.core_ui_country,
        items = filterType.visibleOptions,
        key = { it.code },
        isSelected = { it in filterType.selectedOptions },
        onItemClick = {
          viewModel.onAction(SelectFilterAction.SelectCountry(it))
          onDismissRequest()
        },
        itemName = { stringResource(it.nameRes) + "  ${it.flag}" },
        selected = filterType.selectedOptions.firstOrNull(),
        onValueChange = { viewModel.onAction(SelectFilterAction.SearchFilters(it)) },
        query = filterType.query,
      )
      is FilterType.VoteAverage -> RatingFiltersContent(
        voteAverage = DiscoverFilter.VoteAverage(
          greaterThan = filterType.greaterThan,
          lessThan = filterType.lessThan,
        ),
        minimumVotes = DiscoverFilter.MinimumVotes(filterType.minimumVotes),
        action = viewModel::onAction,
        onDismissRequest = onDismissRequest,
      )
      is FilterType.Year -> YearFilterContent(
        filter = filterType,
        action = { viewModel.onAction(it) },
      )
    }
  }
}
