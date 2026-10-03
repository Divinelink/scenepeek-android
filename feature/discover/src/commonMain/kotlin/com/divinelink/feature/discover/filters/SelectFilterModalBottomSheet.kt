package com.divinelink.feature.discover.filters

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_content_rating
import com.divinelink.core.ui.resources.core_ui_country
import com.divinelink.core.ui.resources.core_ui_language
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.FilterType
import com.divinelink.feature.discover.filters.certification.CertificationFilterList
import com.divinelink.feature.discover.filters.company.CompanyFiltersContent
import com.divinelink.feature.discover.filters.keyword.KeywordsFiltersContent
import com.divinelink.feature.discover.filters.year.YearFilterContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFilterModalBottomSheet(
  type: FilterModal,
  uuid: String,
  mediaType: MediaType,
  viewModel: SelectFilterViewModel = koinViewModel(
    key = "SelectGenreModalBottomSheet-${mediaType.value}-$type-$uuid",
  ) { parametersOf(mediaType, type, uuid) },
  onDismissRequest: () -> Unit,
) {
  val density = LocalDensity.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberBottomSheetState(
    initialValue = Hidden,
    enabledValues = setOf(Hidden, Expanded),
  )

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
        modifier = Modifier.fillMaxHeight(),
        titleRes = UiString.core_ui_language,
        items = filterType.visibleOptions(),
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
        modifier = Modifier.fillMaxHeight(),
        titleRes = UiString.core_ui_country,
        items = filterType.visibleOptions(),
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
        action = viewModel::onAction,
      )
      is FilterType.Keywords -> KeywordsFiltersContent(
        keywords = filterType,
        action = viewModel::onAction,
        onDismissRequest = onDismissRequest,
      )
      is FilterType.Companies -> CompanyFiltersContent(
        companies = filterType,
        action = viewModel::onAction,
        onDismissRequest = onDismissRequest,
      )
      is FilterType.ContentRatings -> CertificationFilterList(
        modifier = Modifier.wrapContentHeight(),
        titleRes = UiString.core_ui_content_rating,
        items = filterType.availableOptions,
        isSelected = { it in filterType.selectedOptions },
        onItemClick = {
          viewModel.onAction(SelectFilterAction.SelectCertification(it))
        },
        selected = filterType.selectedOptions.firstOrNull(),
      )
    }
  }
}
