package com.divinelink.feature.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.home.HomeAction
import org.jetbrains.compose.resources.stringResource

@Composable
fun MediaRow(
  config: HomeSectionInfo,
  form: HomeForm<MediaItem>,
  action: (HomeAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.animateContentSize(),
  ) {
    TextButton(
      onClick = {
        onNavigate(Navigation.MediaListsRoute(config.section))
      },
    ) {
      Text(
        text = stringResource(config.title),
        style = MaterialTheme.typography.titleMedium,
      )

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
      )
    }

    when (form) {
      HomeForm.Initial -> MediaRowSkeleton()
      HomeForm.Error -> MediaRowError(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
        onClick = { action(HomeAction.RetrySection(config.section)) },
      )
      is HomeForm.Data -> MediaRowContent(
        form = form,
        onLoadMore = { action(HomeAction.LoadMore(config.section)) },
        onNavigate = onNavigate,
        onRetry = { action(HomeAction.RetrySection(config.section)) },
      )
    }
  }
}
