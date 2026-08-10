package com.divinelink.feature.discover.filters.company

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.details.CompanyItemCard
import com.divinelink.core.ui.resources.core_ui_clear_all
import com.divinelink.core.ui.resources.core_ui_information_companies
import com.divinelink.core.ui.resources.core_ui_search_companies_placeholder
import com.divinelink.feature.discover.FilterType
import com.divinelink.feature.discover.filters.SelectFilterAction
import com.divinelink.feature.discover.resources.Res
import com.divinelink.feature.discover.resources.feature_discover_empty_company_search_result
import com.divinelink.feature.discover.ui.SearchField
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompanyFiltersContent(
  companies: FilterType.Companies,
  action: (SelectFilterAction) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val state = rememberLazyListState()
  val density = LocalDensity.current
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(state) {
    snapshotFlow { state.isScrollInProgress }
      .distinctUntilChanged()
      .collect { isScrolling ->
        if (isScrolling) {
          keyboardController?.hide()
          focusManager.clearFocus()
        }
      }
  }

  Box {
    var actionsSize by remember { mutableStateOf(0.dp) }
    val items = companies.visibleOptions()

    LazyColumn(
      modifier = Modifier
        .padding(bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8))
        .fillMaxSize(),
      state = state,
    ) {
      item {
        Text(
          modifier = Modifier
            .padding(bottom = MaterialTheme.dimensions.keyline_8)
            .padding(horizontal = MaterialTheme.dimensions.keyline_16),
          textAlign = TextAlign.Start,
          text = pluralStringResource(UiPlurals.core_ui_information_companies, 2),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      stickyHeader {
        SearchField(
          modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(MaterialTheme.dimensions.keyline_16),
          value = companies.query,
          placeholder = stringResource(UiString.core_ui_search_companies_placeholder),
          onValueChange = { action(SelectFilterAction.SearchCompanies(it)) },
        )

        Box {
          AnimatedContent(
            targetState = companies.loading,
          ) { loading ->
            if (loading) {
              AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = companies.loading,
              ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
              }
            } else {
              HorizontalDivider(modifier = Modifier.align(Alignment.Center))
            }
          }
        }
      }

      item {
        if (
          companies.visibleOptions().isEmpty() &&
          !companies.loading &&
          companies.query?.isNotEmpty() == true
        ) {
          BlankSlate(
            uiState = BlankSlateState.Custom(
              title = UIText.StringText(""),
              description = UIText.ResourceText(
                Res.string.feature_discover_empty_company_search_result,
              ),
            ),
          )
        }
      }

      items(
        items = items,
        key = { it.id },
      ) { company ->
        CompanyItemCard(
          company = company,
          selected = company in companies.selectedOptions,
          onClick = { action(SelectFilterAction.SelectCompany(company)) },
        )
      }
    }

    Row(
      modifier = Modifier
        .onSizeChanged { with(density) { actionsSize = it.height.toDp() } }
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ElevatedButton(
        enabled = companies.visibleOptions().isNotEmpty(),
        modifier = Modifier.weight(1f),
        onClick = {
          action(SelectFilterAction.ClearCompanies)
          onDismissRequest()
        },
      ) {
        Text(text = stringResource(UiString.core_ui_clear_all))
      }
    }
  }
}
