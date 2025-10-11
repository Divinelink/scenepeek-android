package com.divinelink.feature.discover.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.feature.discover.R

@Composable
fun DiscoverInitialContent(tab: MediaTab) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = LocalBottomNavigationPadding.current)
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
      text = when (tab) {
        MediaTab.Movie -> stringResource(R.string.feature_discover_movies_title)
        MediaTab.TV -> stringResource(R.string.feature_discover_tv_title)
      },
    )

    Text(
      modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyMedium,
      text = stringResource(R.string.feature_discover_description_first),
    )

    Text(
      modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_4),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      text = stringResource(R.string.feature_discover_description_second),
    )
  }
}
