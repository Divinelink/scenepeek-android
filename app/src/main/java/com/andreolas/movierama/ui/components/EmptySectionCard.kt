package com.andreolas.movierama.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptySectionCard(
  modifier: Modifier = Modifier,
  title: String,
  description: String,
) {
  Material3Card(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    Column {
      Text(
        style = MaterialTheme.typography.titleMedium,
        text = title,
        textAlign = TextAlign.Start,
        modifier = Modifier
          .padding(
            top = 24.dp,
            start = 24.dp
          ),
      )
      Text(
        text = description,
        textAlign = TextAlign.Start,
        modifier = Modifier
          .padding(
            bottom = 24.dp,
            start = 24.dp
          ),
      )
    }
  }
}
