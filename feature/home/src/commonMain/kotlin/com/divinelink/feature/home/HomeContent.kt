package com.divinelink.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.filter.HomeFilter
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.components.FilterBar
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.home.ui.MediaRow

@Composable
fun HomeContent(
  uiState: HomeUiState,
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  scrollState: LazyListState = rememberLazyListState(),
) {
  Column {
    FilterBar(
      modifier = modifier
        .padding(
          horizontal = MaterialTheme.dimensions.keyline_8,
          vertical = MaterialTheme.dimensions.keyline_4,
        ),
      filters = uiState.filters,
      onFilterClick = {
        val route = when (it) {
          is HomeFilter.Saved -> Navigation.MediaListsRoute(MediaListSection.Saved)
          is HomeFilter.TopRated -> Navigation.Discover.Custom(
            mediaType = MediaType.MOVIE,
            entryPoint = DiscoverType.TopRated,
          )
          HomeFilter.Awards -> Navigation.AwardsRoute
        }

        onNavigate(route)
      },
      onClearClick = {
        // Do nothing
      },
    )
    ScenePeekLazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(top = MaterialTheme.dimensions.keyline_16),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      state = scrollState,
    ) {
      items(
        items = uiState.sections,
        key = { it.key },
      ) { config ->
        MediaRow(
          config = config,
          onNavigate = onNavigate,
        )
      }

      item {
        Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
      }
    }
  }
}

@Composable
@Previews
fun HomeContentPreview() {
  PreviewLocalProvider {
    HomeContent(
      uiState = HomeUiState.initial,
      onNavigate = {},
    )
  }
}
