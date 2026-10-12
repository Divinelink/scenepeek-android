package com.divinelink.feature.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.resources.history
import com.divinelink.core.model.tab.ProgressTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.components.IconButtonWithTooltip
import com.divinelink.feature.home.section.HomeSectionAction
import com.divinelink.feature.home.section.SectionViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.divinelink.core.model.resources.Res as ModelRes

@Composable
fun MediaRow(
  config: HomeSectionInfo,
  viewModel: SectionViewModel = koinViewModel(
    key = config.section.key,
    parameters = { parametersOf(config.section) },
  ),
  onNavigate: (Navigation) -> Unit,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(
    modifier = modifier.animateContentSize(),
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      TextButton(
        onClick = {
          val route = when (val section = config.section) {
            MediaListSection.TrendingAll -> Navigation.MediaListsRoute(config.section)
            is MediaListSection.Upcoming -> Navigation.Discover.Custom(
              mediaType = section.mediaType,
              entryPoint = DiscoverType.Upcoming,
            )
            is MediaListSection.Popular -> Navigation.Discover.Custom(
              mediaType = section.mediaType,
              entryPoint = DiscoverType.Popular,
            )
            MediaListSection.RecentRequests -> Navigation.SeerrRequestsRoute(
              filterValue = MediaRequestFilter.All.value,
            )
            MediaListSection.NextUpEpisodes -> Navigation.ProgressScreenRoute(
              ProgressTab.NextUp.order,
            )
          }

          onNavigate(route)
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

      if (config.section is MediaListSection.NextUpEpisodes) {
        Spacer(modifier = Modifier.weight(1f))

        IconButtonWithTooltip(
          onClick = { onNavigate(Navigation.ProgressScreenRoute(ProgressTab.History.order)) },
          tooltipText = ModelRes.string.history,
        ) {
          Icon(
            imageVector = Icons.Default.History,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(ModelRes.string.history),
          )
        }
      }
    }

    when (val form = uiState.form) {
      HomeForm.Initial -> MediaRowSkeleton()
      HomeForm.Error -> MediaRowError(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
        onClick = { viewModel.onAction(HomeSectionAction.RetrySection(config.section)) },
      )
      is HomeForm.Data -> MediaRowContent(
        form = form,
        onLoadMore = { viewModel.onAction(HomeSectionAction.LoadMore(config.section)) },
        onNavigate = onNavigate,
        onRetry = { viewModel.onAction(HomeSectionAction.RetrySection(config.section)) },
      )
      is HomeForm.SeerrData -> SeerrContent(
        filter = form.filter,
        onNavigate = onNavigate,
      )
      is HomeForm.UpNextEpisodes -> NextUpContent(
        onNavigate = onNavigate,
      )
    }
  }
}
