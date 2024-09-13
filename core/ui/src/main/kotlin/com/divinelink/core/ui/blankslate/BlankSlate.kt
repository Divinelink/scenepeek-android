package com.divinelink.core.ui.blankslate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString

@Composable
fun BlankSlate(
  uiState: BlankSlateState,
  onRetry: ((() -> Unit))? = null,
) {
  Column(
    modifier = Modifier
      .testTag(TestTags.BLANK_SLATE)
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    uiState.icon?.let { icon ->
      Image(
        modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_16),
        painter = painterResource(id = icon),
        contentDescription = "Blank slate illustration",
      )
    }

    Text(
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
      text = uiState.title.getString(),
    )

    uiState.description?.let { description ->
      Text(
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        text = description.getString(),
      )
    }

    onRetry?.let {
      Button(
        onClick = it,
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
      ) {
        Text(text = UIText.ResourceText(R.string.core_ui_retry).getString())
      }
    }
  }
}

@Previews
@Composable
private fun BlankSlatePreview() {
  AppTheme {
    Surface {
      BlankSlate(
        uiState = BlankSlateState.Offline,
        onRetry = {},
      )
    }
  }
}
