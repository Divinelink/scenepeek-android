package com.divinelink.core.ui.blankslate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString

@Composable
fun BlankSlate(
  state: BlankSlateState,
  onRetry: ((() -> Unit))? = null,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_16),
      painter = painterResource(R.drawable.core_ui_feeling_blue),
      contentDescription = null,
    )

    Text(
      style = MaterialTheme.typography.titleMedium,
      text = state.title.getString(),
    )

    state.description?.let {
      Text(
        style = MaterialTheme.typography.bodyMedium,
        text = it.getString(),
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
        state = BlankSlateState.Offline,
        onRetry = {},
      )
    }
  }
}
