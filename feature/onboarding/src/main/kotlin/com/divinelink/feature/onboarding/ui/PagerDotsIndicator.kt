package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.conditional

@Composable
fun BoxScope.PagerDotsIndicator(
  totalPages: Int,
  currentIndex: Int,
) {
  if (totalPages <= 1) return

  Row(
    modifier = Modifier
      .padding(bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_32)
      .align(Alignment.BottomCenter),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.Bottom,
  ) {
    for (i in 0..<totalPages) {
      Box(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_6)
          .align(Alignment.CenterVertically)
          .clip(CircleShape)
          .conditional(
            condition = currentIndex == i,
            ifTrue = { size(MaterialTheme.dimensions.keyline_10) },
            ifFalse = { size(MaterialTheme.dimensions.keyline_8) },
          )
          .conditional(
            condition = currentIndex == i,
            ifTrue = { background(MaterialTheme.colorScheme.primary) },
            ifFalse = { background(MaterialTheme.colorScheme.onSurface) },
          ),
      )
    }
  }
}
