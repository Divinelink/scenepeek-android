package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun EmptyContent(text: UIText) {
  Column(
    modifier = Modifier
      .testTag(TestTags.BLANK_SLATE)
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
      text = text.getString(),
    )
  }
}

@Previews
@Composable
private fun CreditsEmptyContentPreview() {
  AppTheme {
    Surface {
      EmptyContent(
        UIText.StringText("No credits available"),
      )
    }
  }
}
