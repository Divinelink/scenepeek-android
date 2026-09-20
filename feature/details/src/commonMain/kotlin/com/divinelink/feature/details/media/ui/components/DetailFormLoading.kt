package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.ui.LoadMoreContent

@Composable
fun DetailFormLoading() {
  Column {
    Spacer(modifier = Modifier.weight(0.2f))
    LoadMoreContent()
    Spacer(modifier = Modifier.weight(1f))
  }
}
