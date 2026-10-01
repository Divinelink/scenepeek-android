package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.commons.util.encodeToString
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Company
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.components.details.CompanyItemCard
import com.divinelink.core.ui.resources.core_ui_information_companies
import org.jetbrains.compose.resources.pluralStringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun CompaniesModal(
  mediaType: MediaType,
  companies: List<Company>,
  onNavigate: (Navigation) -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss,
  ) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
      item {
        Text(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          text = pluralStringResource(UiPlurals.core_ui_information_companies, companies.size),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      items(
        items = companies,
        key = { it.id },
      ) { company ->
        CompanyItemCard(
          company = company,
          selected = false,
          onClick = {
            onDismiss()
            onNavigate(
              Navigation.Discover.Default(
                mediaType = mediaType.value,
                encodedCompany = company.encodeToString(),
              ),
            )
          },
        )
      }
    }
  }
}
