package com.andreolas.movierama.settings.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
  modifier: Modifier = Modifier,
  title: String,
  onNavigationClick: () -> Unit,
  navigationIconPainter: ImageVector = Icons.AutoMirrored.Rounded.ArrowBack,
  navigationContentDescription: String? = null,
  content: @Composable (PaddingValues) -> Unit
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigationClick) {
            Icon(
              imageVector = navigationIconPainter,
              contentDescription = navigationContentDescription
            )
          }
        },
        scrollBehavior = scrollBehavior,
        colors = topAppBarColors()
      )
    },
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    content = content
  )
}
