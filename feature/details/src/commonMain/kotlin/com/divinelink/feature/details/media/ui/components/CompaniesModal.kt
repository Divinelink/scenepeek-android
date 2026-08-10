package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.commons.util.encodeToString
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.details.Company
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.coil.Image
import com.divinelink.core.ui.resources.core_ui_information_companies
import org.jetbrains.compose.resources.pluralStringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
        Row(
          modifier = Modifier
            .clickable {
              onDismiss()
              onNavigate(
                Navigation.DiscoverRoute(
                  entryPointUuid = Uuid.random().toHexString(),
                  mediaType = mediaType.value,
                  encodedCompany = company.encodeToString(),
                ),
              )
            }
            .padding(MaterialTheme.dimensions.keyline_16),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Image(
            modifier = Modifier
              .size(MaterialTheme.dimensions.keyline_64)
              .clip(CircleShape)
              .background(
                if (company.logoPath.isBlank()) {
                  BottomSheetDefaults.ContainerColor
                } else {
                  if (LocalDarkThemeProvider.current) {
                    MaterialTheme.colorScheme.onSurface
                  } else {
                    BottomSheetDefaults.ContainerColor
                  }
                },
              )
              .padding(MaterialTheme.dimensions.keyline_8),
            path = company.logoPath,
            contentScale = ContentScale.Inside,
            quality = ImageQuality.COMPANY_LOGO,
          )

          Text(
            text = company.name + "  ${company.country.flag}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}
