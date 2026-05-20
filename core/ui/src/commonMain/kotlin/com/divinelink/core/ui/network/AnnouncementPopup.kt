package com.divinelink.core.ui.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.divinelink.core.model.config.ConfigMessage

@Composable
fun AnnouncementPopup(configMessage: ConfigMessage) {
  AnimatedVisibility(
    visible = !configMessage.dismissable,
  ) {
    StatusMessage(
      text = configMessage.content,
      containerColor = MaterialTheme.colorScheme.primary,
    )
  }
}
