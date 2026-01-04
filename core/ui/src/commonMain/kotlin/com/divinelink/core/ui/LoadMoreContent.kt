package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.components.Material3CircularProgressIndicator

@Composable
fun LoadMoreContent(modifier: Modifier = Modifier) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .padding(vertical = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    Material3CircularProgressIndicator(
      modifier = Modifier.wrapContentSize(),
    )
  }
}
