package com.divinelink.core.ui.components.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.details.Company
import com.divinelink.core.ui.coil.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyItemScope.CompanyItemCard(
  company: Company,
  selected: Boolean,
  onClick: (Company) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .animateItem()
      .clickable { onClick(company) }
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_20,
        vertical = MaterialTheme.dimensions.keyline_8,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (company.logoPath.isNotBlank()) {
      Image(
        modifier = Modifier
          .size(MaterialTheme.dimensions.keyline_64)
          .clip(CircleShape)
          .background(
            if (LocalDarkThemeProvider.current) {
              MaterialTheme.colorScheme.onSurface
            } else {
              BottomSheetDefaults.ContainerColor
            },
          )
          .padding(MaterialTheme.dimensions.keyline_8),
        path = company.logoPath,
        contentScale = ContentScale.Inside,
        quality = ImageQuality.COMPANY_LOGO,
      )
    }

    Text(
      modifier = Modifier
        .weight(1f)
        .padding(MaterialTheme.dimensions.keyline_8),
      text = company.name + "  ${company.country.flag}",
      style = MaterialTheme.typography.bodyMedium,
    )

    AnimatedVisibility(selected) {
      Icon(
        imageVector = Icons.Default.Done,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = null,
      )
    }
  }

  HorizontalDivider(
    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
  )
}
