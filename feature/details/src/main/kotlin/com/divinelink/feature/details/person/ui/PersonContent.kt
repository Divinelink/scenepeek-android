package com.divinelink.feature.details.person.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.CreditMediaItem
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.core.ui.nestedscroll.rememberCollapsingContentNestedScrollConnection
import com.divinelink.feature.details.R
import com.divinelink.feature.details.person.ui.credits.KnownForSection
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider
import com.divinelink.feature.details.person.ui.tab.PersonTabs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.divinelink.core.ui.R as uiR

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun PersonContent(
  scope: CoroutineScope,
  uiState: PersonUiState,
  connection: CollapsingContentNestedScrollConnection,
  paddingValues: PaddingValues,
  lazyListState: LazyListState,
  onMediaClick: (MediaItem) -> Unit,
  onTabSelected: (Int) -> Unit,
  onUpdateLayoutStyle: () -> Unit,
) {
  var selectedPage by rememberSaveable { mutableIntStateOf(0) }
  val isGrid = uiState.layoutStyle == LayoutStyle.GRID
  val icon = if (isGrid) Icons.AutoMirrored.Outlined.List else Icons.Outlined.GridView
  val grid = if (isGrid) GridCells.Fixed(3) else GridCells.Fixed(1)
  uiState.personDetails as PersonDetailsUiState.Data

  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      onTabSelected(page)
      selectedPage = page
    }
  }

  Box(Modifier.nestedScroll(connection)) {
    CollapsiblePersonContent(
      paddingValues = paddingValues,
      connection = connection,
      personDetails = uiState.personDetails,
    )

    ScenePeekLazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .offset { IntOffset(0, connection.currentSize.roundToPx()) }
        .padding(top = paddingValues.calculateTopPadding())
        .testTag(TestTags.Person.CONTENT_LIST),
      state = lazyListState,
    ) {
      stickyHeader {
        PersonTabs(
          tabs = uiState.tabs,
          selectedIndex = selectedPage,
          onClick = {
            onTabSelected(it)
            scope.launch {
              pagerState.animateScrollToPage(it)
            }
          },
        )
      }

      item {
        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
        ) { page ->
          uiState.forms.values.elementAt(page).let { form ->
            when (form) {
              is PersonForm.About -> LazyColumn(
                modifier = Modifier.fillParentMaxSize(),
              ) {
                item {
                  PersonalDetails(uiState.personDetails)
                }

                item {
                  KnownForSection(
                    list = uiState.knownForCredits ?: emptyList(),
                    onMediaClick = onMediaClick,
                  )
                }
              }
              is PersonForm.Movies -> LazyVerticalGrid(
                columns = grid,
                modifier = Modifier.fillParentMaxSize(),
                contentPadding = PaddingValues(
                  start = MaterialTheme.dimensions.keyline_8,
                  end = MaterialTheme.dimensions.keyline_8,
                  top = MaterialTheme.dimensions.keyline_8,
                  bottom = MaterialTheme.dimensions.keyline_16,
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                  Row {
                    Text(
                      modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimensions.keyline_16,
                        vertical = MaterialTheme.dimensions.keyline_8,
                      ),
                      style = MaterialTheme.typography.titleSmall,
                      text = pluralStringResource(
                        id = R.plurals.feature_details_movies,
                        count = form.data.size,
                        form.data.size,
                      ),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    LayoutStyleButton(
                      onUpdateLayoutStyle = onUpdateLayoutStyle,
                      icon = icon,
                    )
                  }
                }

                items(
                  items = form.data,
                  key = { it.mediaItem.id },
                ) { item ->
                  if (isGrid) {
                    MediaItem(
                      modifier = Modifier
                        .animateItem()
                        .animateContentSize(),
                      media = item.mediaItem,
                      subtitle = item.role.title,
                      fullDate = false,
                      onMediaItemClick = onMediaClick,
                    ) { }
                  } else {
                    CreditMediaItem(
                      modifier = Modifier
                        .animateItem()
                        .animateContentSize(),
                      mediaItem = item.mediaItem,
                      subtitle = item.role.title,
                      onClick = onMediaClick,
                    )
                  }
                }
              }

              is PersonForm.TvShows -> LazyVerticalGrid(
                columns = grid,
                modifier = Modifier.fillParentMaxSize(),
                contentPadding = PaddingValues(
                  start = MaterialTheme.dimensions.keyline_8,
                  end = MaterialTheme.dimensions.keyline_8,
                  top = MaterialTheme.dimensions.keyline_8,
                  bottom = MaterialTheme.dimensions.keyline_16,
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
              ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                  Row {
                    Text(
                      modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimensions.keyline_16,
                        vertical = MaterialTheme.dimensions.keyline_8,
                      ),
                      style = MaterialTheme.typography.titleSmall,
                      text = pluralStringResource(
                        id = R.plurals.feature_details_tv_shows,
                        count = form.data.size,
                        form.data.size,
                      ),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    LayoutStyleButton(
                      onUpdateLayoutStyle = onUpdateLayoutStyle,
                      icon = icon,
                    )
                  }
                }

                items(
                  items = form.data,
                  key = { it.mediaItem.id },
                ) { item ->
                  if (isGrid) {
                    MediaItem(
                      modifier = Modifier
                        .animateItem()
                        .animateContentSize(),
                      media = item.mediaItem,
                      subtitle = item.role.title,
                      fullDate = false,
                      onMediaItemClick = onMediaClick,
                    ) { }
                  } else {
                    CreditMediaItem(
                      modifier = Modifier
                        .animateItem()
                        .animateContentSize(),
                      mediaItem = item.mediaItem,
                      subtitle = item.role.title,
                      onClick = onMediaClick,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LayoutStyleButton(
  onUpdateLayoutStyle: () -> Unit,
  icon: ImageVector,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(
      MaterialTheme.dimensions.keyline_4,
    ),
    modifier = Modifier
      .clip(shape = MaterialTheme.shapes.large)
      .clickable { onUpdateLayoutStyle() }
      .padding(
        horizontal = MaterialTheme.dimensions.keyline_8,
        vertical = MaterialTheme.dimensions.keyline_4,
      ),
  ) {
    Text(
      text = stringResource(uiR.string.core_ui_view),
      color = MaterialTheme.colorScheme.primary,
    )
    Icon(
      imageVector = icon,
      contentDescription = stringResource(uiR.string.core_ui_change_layout_button),
      tint = MaterialTheme.colorScheme.primary,
    )
  }
}

@Composable
private fun CollapsiblePersonContent(
  paddingValues: PaddingValues,
  connection: CollapsingContentNestedScrollConnection,
  personDetails: PersonDetailsUiState.Data,
) {
  Column(
    modifier = Modifier
      .padding(top = paddingValues.calculateTopPadding())
      .verticalScroll(state = rememberScrollState())
      .fillMaxWidth()
      .graphicsLayer {
        alpha = (connection.currentSize / connection.maxHeight)
        translationY = -(connection.maxHeight.toPx() - connection.currentSize.toPx())
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      style = MaterialTheme.typography.displaySmall,
      text = personDetails.personDetails.person.name,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    MovieImage(
      modifier = Modifier.height(MaterialTheme.dimensions.posterSize),
      path = personDetails.personDetails.person.profilePath,
      errorPlaceHolder = if (
        personDetails.personDetails.person.gender == Gender.FEMALE
      ) {
        painterResource(id = uiR.drawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(id = uiR.drawable.core_ui_ic_person_placeholder)
      },
    )
  }
}

@Previews
@Composable
fun PersonContentPreview(
  @PreviewParameter(PersonUiStatePreviewParameterProvider::class) uiState: PersonUiState,
) {
  AppTheme {
    Surface {
      PersonContent(
        uiState = uiState,
        connection = rememberCollapsingContentNestedScrollConnection(256.dp),
        paddingValues = PaddingValues(0.dp),
        lazyListState = rememberLazyListState(),
        scope = rememberCoroutineScope(),
        onMediaClick = {},
        onTabSelected = {},
        onUpdateLayoutStyle = {},
      )
    }
  }
}
