package com.divinelink.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@Composable
fun ObfuscateTotalEpisodesButton(
  episodesObfuscated: Boolean,
  onClick: () -> Unit,
) {
  IconButton(
    modifier = Modifier.testTag(TestTags.Menu.EPISODES_OBFUSCATION),
    onClick = onClick,
  ) {
    if (episodesObfuscated) {
      Icon(
        painter = painterResource(id = R.drawable.core_ui_eye_off),
        contentDescription = stringResource(R.string.core_ui_hidden_total_episodes_button),
      )
    } else {
      Icon(
        painter = painterResource(R.drawable.core_ui_eye),
        contentDescription = stringResource(R.string.core_ui_visible_total_episodes_button),
      )
    }
  }
}
