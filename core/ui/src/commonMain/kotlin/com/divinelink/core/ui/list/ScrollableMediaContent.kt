package com.divinelink.core.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.LoadMoreContent
import com.divinelink.core.ui.ScreenSettingsRow
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.details.cast.CreditsItemCard
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.composition.rememberViewModePreferences
import com.divinelink.core.ui.credit.PersonItem
import kotlinx.coroutines.launch

@Composable
fun ScrollableMediaContent(
  modifier: Modifier = Modifier,
  state: LazyGridState = rememberLazyGridState(),
  items: List<MediaItem>,
  section: ViewableSection,
  onLoadMore: () -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
  onClick: (MediaItem) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
  canLoadMore: Boolean,
  emptyContent: @Composable () -> Unit = {},
) {
  val scope = rememberCoroutineScope()
  val viewMode = rememberViewModePreferences(section)

  val columns = when (viewMode) {
    ViewMode.GRID -> GridCells.Adaptive(mediaCardSize())
    ViewMode.LIST -> GridCells.Fixed(1)
  }

  state.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = onLoadMore,
  )

  Box(Modifier.fillMaxSize()) {
    LazyVerticalGrid(
      modifier = modifier
        .fillMaxSize()
        .testTag(TestTags.Components.MEDIA_LIST_CONTENT),
      columns = columns,
      contentPadding = PaddingValues(
        start = MaterialTheme.dimensions.keyline_8,
        end = MaterialTheme.dimensions.keyline_8,
        top = MaterialTheme.dimensions.keyline_4,
        bottom = LocalBottomNavigationPadding.current,
      ),
      state = state,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      if (items.isEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          emptyContent()
        }
      } else {
        item(span = { GridItemSpan(maxLineSpan) }) {
          ScreenSettingsRow(
            section = section,
            onSwitchViewMode = onSwitchViewMode,
          )
        }

        items(
          items = items,
          key = { it.uniqueIdentifier },
        ) { media ->
          when (media) {
            is MediaItem.Media -> when (viewMode) {
              ViewMode.GRID -> MediaItem(
                modifier = Modifier
                  .animateItem()
                  .animateContentSize(),
                media = media,
                onClick = onClick,
                onLongClick = onLongClick,
                showDate = section == ViewableSection.SEARCH || section == ViewableSection.DISCOVER,
              )
              ViewMode.LIST -> DetailedMediaItem(
                modifier = Modifier
                  .animateItem()
                  .animateContentSize(),
                mediaItem = media,
                onClick = onClick,
                onLongClick = onLongClick,
              )
            }
            is MediaItem.Person -> when (viewMode) {
              ViewMode.GRID -> CreditsItemCard(
                modifier = Modifier
                  .animateItem()
                  .animateContentSize(),
                person = Person(
                  id = media.id.toLong(),
                  name = media.name,
                  profilePath = media.posterPath,
                  gender = media.gender,
                  knownForDepartment = media.knownForDepartment,
                  role = listOf(PersonRole.Unknown),
                ),
                onPersonClick = { onClick(media) },
              )
              ViewMode.LIST -> PersonItem(
                modifier = Modifier
                  .animateItem()
                  .animateContentSize(),
                person = Person(
                  id = media.id.toLong(),
                  name = media.name,
                  profilePath = media.posterPath,
                  gender = media.gender,
                  knownForDepartment = media.knownForDepartment,
                  role = listOf(PersonRole.Unknown),
                ),
                onClick = { onClick(media) },
                isObfuscated = false,
              )
            }
            MediaItem.Unknown -> Unit
          }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
          AnimatedVisibility(visible = canLoadMore) {
            LoadMoreContent()
          }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
          Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
        }
      }
    }

    ScrollToTopButton(
      modifier = Modifier.align(Alignment.BottomCenter),
      visible = state.canScrollToTop(),
      onClick = { scope.launch { state.animateScrollToItem(0) } },
    )
  }
}
