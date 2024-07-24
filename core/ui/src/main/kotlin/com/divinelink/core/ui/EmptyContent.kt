package com.divinelink.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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

data class EmptyContentUiState(
  val icon: Int? = null,
  val title: UIText = UIText.StringText(""),
  val description: UIText = UIText.StringText(""),
)

@Composable
fun EmptyContent(uiState: EmptyContentUiState) {
  Column(
    modifier = Modifier
      .testTag(TestTags.BLANK_SLATE)
      .fillMaxSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    uiState.icon?.let { icon ->
      Icon(
        modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_16),
        painter = painterResource(id = icon),
        contentDescription = null,
      )
    }

    Text(
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
      text = uiState.title.getString(),
    )

    if (uiState.description.getString().isNotEmpty()) {
      Text(
        modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        text = uiState.description.getString(),
      )
    }
  }
}

@Previews
@Composable
private fun CreditsEmptyContentPreview() {
  AppTheme {
    Surface {
      EmptyContent(
        uiState = EmptyContentUiState(
          icon = R.drawable.core_ui_ic_error_64,
          title = UIText.StringText("No credits available"),
          description = UIText.StringText("You haven't added any credits yet."),
        ),
      )
    }
  }
}
