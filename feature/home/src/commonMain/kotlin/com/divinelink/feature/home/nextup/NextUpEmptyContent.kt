package com.divinelink.feature.home.nextup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.show_progress_empty_state_subtitle
import com.divinelink.core.ui.resources.show_progress_empty_state_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NextUpEmptyContent(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .padding(horizontal = MaterialTheme.dimensions.keyline_12),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      text = stringResource(UiString.show_progress_empty_state_title),
      style = MaterialTheme.typography.titleSmall,
    )
    Text(
      text = stringResource(UiString.show_progress_empty_state_subtitle),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}
