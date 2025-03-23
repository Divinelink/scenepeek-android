package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.R
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.getString

@Composable
fun SuccessText(text: UIText) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = Color(0xFF4CAF50), shape = MaterialTheme.shapes.medium)
      .padding(MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.spacedBy(
      space = MaterialTheme.dimensions.keyline_8,
      alignment = Alignment.CenterHorizontally,
    ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = Color.White,
    )

    Text(
      text = text.getString(),
      color = Color.White,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Previews
@Composable
fun SuccessTextPreview() {
  AppTheme {
    SuccessText(
      text = UIText.ResourceText(R.string.core_model_onboarding_successfully_connected),
    )
  }
}
