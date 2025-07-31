package com.divinelink.feature.add.to.account.modal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.add.to.account.modal.ActionMenuIntent
import com.divinelink.feature.add.to.account.modal.ActionMenuUiState

@Composable
fun ActionMenuContent(
  uiState: ActionMenuUiState,
  onAction: (ActionMenuIntent) -> Unit,
) {
  LazyColumn {
    item {
      Text(
        modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
        text = uiState.media.name,
        style = MaterialTheme.typography.headlineSmall,
      )
      HorizontalDivider()
      Spacer(modifier = Modifier.padding(MaterialTheme.dimensions.keyline_4))
    }
    items(
      items = uiState.availableActions,
      key = { it.name },
    ) { action ->
      Row(
        modifier = Modifier
          .clickable {
            onAction(action)
          }
          .padding(MaterialTheme.dimensions.keyline_16),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = action.icon,
          contentDescription = null,
        )

        Text(
          text = stringResource(action.textRes),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.weight(1f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}
