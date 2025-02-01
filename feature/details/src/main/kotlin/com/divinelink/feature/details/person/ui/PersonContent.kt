package com.divinelink.feature.details.person.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.core.ui.nestedscroll.rememberCollapsingContentNestedScrollConnection
import com.divinelink.feature.details.person.ui.credits.KnownForSection
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.filter.CreditFilterButton
import com.divinelink.feature.details.person.ui.filter.CreditsFilterModalBottomSheet
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider
import com.divinelink.feature.details.person.ui.tab.PersonTab
import com.divinelink.feature.details.person.ui.tab.PersonTabs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.divinelink.core.ui.R as uiR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PersonContent(
  scope: CoroutineScope,
  uiState: PersonUiState,
  connection: CollapsingContentNestedScrollConnection,
  lazyListState: LazyListState,
  onMediaClick: (MediaItem) -> Unit,
  onTabSelected: (Int) -> Unit,
  onUpdateLayoutStyle: () -> Unit,
  onApplyFilter: (CreditFilter) -> Unit,
) {
  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTabIndex) }
  val isGrid = uiState.layoutStyle == LayoutStyle.GRID
  val icon = if (isGrid) Icons.AutoMirrored.Outlined.List else Icons.Outlined.GridView
  val grid = if (isGrid) {
    GridCells.Adaptive(MaterialTheme.dimensions.shortMediaCard)
  } else {
    GridCells.Fixed(1)
  }

  val tvLazyGridState = rememberLazyGridState()
  val movieLazyGridState = rememberLazyGridState()

  val movieFilters = remember(uiState.filters) {
    uiState.filters[PersonTab.MOVIES.order] ?: emptyList()
  }
  val tvFilters = remember(uiState.filters) {
    uiState.filters[PersonTab.TV_SHOWS.order] ?: emptyList()
  }

  val filters = uiState.filters[selectedPage] ?: emptyList()

  val movies by remember(uiState.filteredCredits[PersonTab.MOVIES.order]) {
    derivedStateOf { uiState.filteredCredits[PersonTab.MOVIES.order] ?: emptyMap() }
  }

  val tvShows by remember(uiState.filteredCredits[PersonTab.TV_SHOWS.order]) {
    derivedStateOf { uiState.filteredCredits[PersonTab.TV_SHOWS.order] ?: emptyMap() }
  }

  val showFilterTab = when (uiState.forms.values.elementAt(selectedPage)) {
    is PersonForm.Movies -> movies.isNotEmpty()
    is PersonForm.TvShows -> tvShows.isNotEmpty()
    else -> false
  }

  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  val filterBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

  var currentMovieDepartment by rememberSaveable { mutableStateOf("") }
  var currentTvDepartment by rememberSaveable { mutableStateOf("") }

  val personDetails = remember(uiState.aboutForm) {
    uiState.aboutForm?.personDetails
  }

  val department = when (selectedPage) {
    PersonTab.MOVIES.order -> currentMovieDepartment
    PersonTab.TV_SHOWS.order -> currentTvDepartment
    else -> ""
  }

  LaunchedEffect(showFilterBottomSheet) {
    if (showFilterBottomSheet) {
      filterBottomSheetState.show()
    } else {
      filterBottomSheetState.hide()
    }
  }

  if (showFilterBottomSheet) {
    CreditsFilterModalBottomSheet(
      sheetState = filterBottomSheetState,
      appliedFilters = uiState.filters[selectedPage] ?: emptyList(),
      filters = uiState.forms.values.elementAt(selectedPage).availableFilters,
      onClick = { filter ->
        onApplyFilter(filter)
        showFilterBottomSheet = false
      },
      onDismissRequest = {
        showFilterBottomSheet = false
      },
    )
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        onTabSelected(page)
      }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(connection),
  ) {
    CollapsiblePersonContent(
      connection = connection,
      personDetails = personDetails as PersonDetailsUiState.Data,
    )

    ScenePeekLazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .offset { IntOffset(0, connection.currentSize.roundToPx()) }
        .testTag(TestTags.Person.CONTENT_LIST),
      state = lazyListState,
    ) {
      stickyHeader {
        Column {
          PersonTabs(
            tabs = uiState.tabs,
            selectedIndex = selectedPage,
            onClick = {
              scope.launch {
                pagerState.animateScrollToPage(it)
              }
            },
          )
          AnimatedVisibility(visible = showFilterTab) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              CreditFilterButton(
                appliedFilters = filters,
                onFilterClick = {
                  showFilterBottomSheet = true
                },
              )

              Spacer(modifier = Modifier.weight(1f))

              if (filters.isEmpty()) {
                AnimatedContent(
                  modifier = Modifier.testTag(
                    TestTags.Person.DEPARTMENT_STICKY_HEADER.format(department),
                  ),
                  targetState = department,
                  label = "DepartmentTextAnimation",
                ) { string ->
                  Text(text = string)
                }
              }

              Spacer(modifier = Modifier.weight(1f))

              LayoutStyleButton(
                onUpdateLayoutStyle = onUpdateLayoutStyle,
                icon = icon,
              )
            }
          }
        }
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
                modifier = Modifier
                  .testTag(TestTags.Person.ABOUT_FORM)
                  .fillParentMaxSize(),
              ) {
                item {
                  PersonalDetails(personDetails)
                }

                item {
                  KnownForSection(
                    list = uiState.knownForCredits ?: emptyList(),
                    onMediaClick = onMediaClick,
                  )
                }
              }

              is PersonForm.Movies -> PersonGridContent(
                modifier = Modifier
                  .fillParentMaxSize()
                  .testTag(TestTags.Person.MOVIES_FORM.format(isGrid)),
                grid = grid,
                credits = movies,
                filters = movieFilters,
                isGrid = isGrid,
                onMediaClick = onMediaClick,
                setCurrentDepartment = { currentMovieDepartment = it },
                mediaType = MediaType.MOVIE,
                name = personDetails.personDetails.person.name,
                lazyGridState = movieLazyGridState,
              )

              is PersonForm.TvShows -> PersonGridContent(
                modifier = Modifier
                  .fillParentMaxSize()
                  .testTag(TestTags.Person.TV_SHOWS_FORM.format(isGrid)),
                grid = grid,
                credits = tvShows,
                filters = tvFilters,
                isGrid = isGrid,
                onMediaClick = onMediaClick,
                setCurrentDepartment = { currentTvDepartment = it },
                mediaType = MediaType.TV,
                name = personDetails.personDetails.person.name,
                lazyGridState = tvLazyGridState,
              )
            }
          }
        }
      }
    }

    ScrollToTopButton(
      modifier = Modifier.align(Alignment.BottomCenter),
      visible = when (selectedPage) {
        PersonTab.MOVIES.order -> movieLazyGridState.canScrollToTop()
        PersonTab.TV_SHOWS.order -> tvLazyGridState.canScrollToTop()
        else -> false
      },
      onClick = {
        scope.launch {
          when (selectedPage) {
            PersonTab.MOVIES.order -> {
              movieLazyGridState.animateScrollToItem(0)
              lazyListState.animateScrollToItem(0)
            }
            PersonTab.TV_SHOWS.order -> {
              tvLazyGridState.animateScrollToItem(0)
              lazyListState.animateScrollToItem(0)
            }
            else -> {
              // Do nothing
            }
          }
        }
      },
    )
  }
}

@Composable
private fun LayoutStyleButton(
  onUpdateLayoutStyle: () -> Unit,
  icon: ImageVector,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    modifier = Modifier
      .clip(shape = MaterialTheme.shapes.large)
      .testTag(TestTags.Person.LAYOUT_SWITCHER)
      .clickable(onClick = onUpdateLayoutStyle)
      .padding(
        vertical = MaterialTheme.dimensions.keyline_8,
        horizontal = MaterialTheme.dimensions.keyline_16,
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
  connection: CollapsingContentNestedScrollConnection,
  personDetails: PersonDetailsUiState.Data,
) {
  Column(
    modifier = Modifier
      .verticalScroll(state = rememberScrollState())
      .testTag(TestTags.Person.COLLAPSIBLE_CONTENT)
      .fillMaxWidth()
      .graphicsLayer {
        alpha = (connection.currentSize / connection.maxHeight)
        translationY = -(connection.maxHeight.toPx() - connection.currentSize.toPx())
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier
        .testTag(TestTags.Person.PERSON_NAME)
        .padding(horizontal = MaterialTheme.dimensions.keyline_16),
      style = MaterialTheme.typography.displaySmall,
      text = personDetails.personDetails.person.name,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    MovieImage(
      modifier = Modifier.height(MaterialTheme.dimensions.posterSize),
      path = personDetails.personDetails.person.profilePath,
      errorPlaceHolder = if (personDetails.personDetails.person.gender == Gender.FEMALE) {
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
  val lazyListState = rememberLazyListState()
  LaunchedEffect(Unit) {
    lazyListState.scrollToItem(0)
  }
  AppTheme {
    Surface {
      PersonContent(
        uiState = uiState,
        connection = rememberCollapsingContentNestedScrollConnection(256.dp),
        lazyListState = lazyListState,
        scope = rememberCoroutineScope(),
        onMediaClick = {},
        onTabSelected = {},
        onUpdateLayoutStyle = {},
        onApplyFilter = {},
      )
    }
  }
}
