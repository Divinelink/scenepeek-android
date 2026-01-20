package com.divinelink.feature.details.person.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.button.switchview.SwitchViewButton
import com.divinelink.core.ui.coil.PosterImage
import com.divinelink.core.ui.collapsingheader.CollapsingHeaderLayout
import com.divinelink.core.ui.collapsingheader.rememberCollapsingHeaderState
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.composition.rememberViewModePreferences
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.resources.core_ui_ic_female_person_placeholder
import com.divinelink.core.ui.resources.core_ui_ic_person_placeholder
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.details.person.ui.credits.KnownForSection
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.filter.CreditFilterButton
import com.divinelink.feature.details.person.ui.filter.CreditsFilterModalBottomSheet
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SharedTransitionScope.PersonContent(
  scope: CoroutineScope,
  uiState: PersonUiState,
  lazyListState: LazyListState,
  onMediaClick: (MediaItem) -> Unit,
  onTabSelected: (Int) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  onApplyFilter: (CreditFilter) -> Unit,
  onProgressUpdate: (Float) -> Unit,
  onNavigate: (Navigation) -> Unit,
  visibilityScope: AnimatedVisibilityScope,
) {
  val density = LocalDensity.current
  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTabIndex) }
  val isGrid = rememberViewModePreferences(ViewableSection.PERSON_CREDITS) == ViewMode.GRID

  val grid = if (isGrid) {
    GridCells.Adaptive(mediaCardSize())
  } else {
    GridCells.Fixed(1)
  }

  val tvLazyGridState = rememberLazyGridState()
  val movieLazyGridState = rememberLazyGridState()

  val movieFilters = remember(uiState.filters) {
    uiState.filters[PersonTab.Movies.order] ?: emptyList()
  }
  val tvFilters = remember(uiState.filters) {
    uiState.filters[PersonTab.TVShows.order] ?: emptyList()
  }

  val filters = uiState.filters[selectedPage] ?: emptyList()

  val movies by remember(uiState.filteredCredits[PersonTab.Movies.order]) {
    derivedStateOf { uiState.filteredCredits[PersonTab.Movies.order] ?: emptyMap() }
  }

  val tvShows by remember(uiState.filteredCredits[PersonTab.TVShows.order]) {
    derivedStateOf { uiState.filteredCredits[PersonTab.TVShows.order] ?: emptyMap() }
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
    PersonTab.Movies.order -> currentMovieDepartment
    PersonTab.TVShows.order -> currentTvDepartment
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

  val collapsingHeaderState = rememberCollapsingHeaderState(
    collapsedHeight = with(density) { MaterialTheme.dimensions.keyline_0.toPx() },
    initialExpandedHeight = with(density) { 400.dp.toPx() },
  )

  LaunchedEffect(collapsingHeaderState.progress) {
    onProgressUpdate(collapsingHeaderState.progress)
  }

  Box {
    CollapsingHeaderLayout(
      modifier = Modifier
        .testTag(TestTags.Person.COLLAPSIBLE_CONTENT)
        .fillMaxSize(),
      state = collapsingHeaderState,
      headerContent = {
        CollapsiblePersonContent(
          modifier = Modifier
            .offset {
              IntOffset(
                x = 0,
                y = -collapsingHeaderState.translation.roundToInt(),
              )
            }
            .fillMaxWidth(),
          personDetails = personDetails as PersonDetailsUiState.Data,
          onNavigate = onNavigate,
          visibilityScope = visibilityScope,
        )
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .testTag(TestTags.Person.CONTENT_LIST),
      ) {
        Column {
          ScenePeekTabs(
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
              horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
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
                  modifier = Modifier
                    .testTag(TestTags.Person.DEPARTMENT_STICKY_HEADER.format(department)),
                  targetState = department,
                  label = "DepartmentTextAnimation",
                ) { string ->
                  Text(
                    text = string,
                    style = MaterialTheme.typography.titleSmall,
                  )
                }
              }

              Spacer(modifier = Modifier.weight(1f))

              SwitchViewButton(
                onClick = { onSwitchPreferences(SwitchPreferencesAction.SwitchViewMode(it)) },
                section = ViewableSection.PERSON_CREDITS,
              )
            }
          }
        }

        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
        ) { page ->
          personDetails as PersonDetailsUiState.Data

          uiState.forms.values.elementAt(page).let { form ->
            when (form) {
              is PersonForm.About -> LazyColumn(
                modifier = Modifier
                  .testTag(TestTags.Person.ABOUT_FORM)
                  .fillMaxSize(),
              ) {
                item {
                  PersonalDetails(personDetails)
                }

                if (!uiState.knownForCredits.isNullOrEmpty()) {
                  item {
                    KnownForSection(
                      list = uiState.knownForCredits,
                      onMediaClick = onMediaClick,
                      onNavigate = onNavigate,
                    )
                  }
                }

                item {
                  Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
                }
              }

              is PersonForm.Movies -> PersonGridContent(
                modifier = Modifier
                  .fillMaxSize()
                  .testTag(TestTags.Person.MOVIES_FORM.format(isGrid)),
                grid = grid,
                credits = movies,
                filters = movieFilters,
                isGrid = isGrid,
                onNavigate = onNavigate,
                setCurrentDepartment = { currentMovieDepartment = it },
                mediaType = MediaType.MOVIE,
                name = personDetails.personDetails.person.name,
                lazyGridState = movieLazyGridState,
              )

              is PersonForm.TvShows -> PersonGridContent(
                modifier = Modifier
                  .fillMaxSize()
                  .testTag(TestTags.Person.TV_SHOWS_FORM.format(isGrid)),
                grid = grid,
                credits = tvShows,
                filters = tvFilters,
                isGrid = isGrid,
                onNavigate = onNavigate,
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
        PersonTab.Movies.order -> movieLazyGridState.canScrollToTop()
        PersonTab.TVShows.order -> tvLazyGridState.canScrollToTop()
        else -> false
      },
      onClick = {
        scope.launch {
          when (selectedPage) {
            PersonTab.Movies.order -> {
              movieLazyGridState.animateScrollToItem(0)
              lazyListState.animateScrollToItem(0)
            }
            PersonTab.TVShows.order -> {
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
private fun SharedTransitionScope.CollapsiblePersonContent(
  modifier: Modifier = Modifier,
  personDetails: PersonDetailsUiState.Data,
  onNavigate: (Navigation) -> Unit,
  visibilityScope: AnimatedVisibilityScope,
) {
  Column(
    modifier = modifier
      .verticalScroll(state = rememberScrollState())
      .testTag(TestTags.Person.COLLAPSIBLE_CONTENT)
      .fillMaxWidth(),
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

    PosterImage(
      modifier = Modifier
        .sharedElement(
          sharedContentState = rememberSharedContentState(
            SharedElementKeys.MediaPoster(personDetails.personDetails.person.profilePath ?: ""),
          ),
          animatedVisibilityScope = visibilityScope,
        )
        .height(MaterialTheme.dimensions.posterSize)
        .aspectRatio(2f / 3f),
      path = personDetails.personDetails.person.profilePath,
      errorPlaceHolder = if (personDetails.personDetails.person.gender == Gender.FEMALE) {
        painterResource(UiDrawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(UiDrawable.core_ui_ic_person_placeholder)
      },
      onClick = { onNavigate(Navigation.MediaPosterRoute(it)) },
      quality = ImageQuality.QUALITY_342,
    )
  }
}

@Previews
@Composable
fun PersonContentListPreview(
  @PreviewParameter(PersonUiStatePreviewParameterProvider::class) uiState: PersonUiState,
) {
  val lazyListState = rememberLazyListState()
  LaunchedEffect(Unit) {
    lazyListState.scrollToItem(0)
  }

  val appState = rememberScenePeekAppState(
    networkMonitor = TestNetworkMonitor(),
    onboardingManager = TestOnboardingManager(),
    preferencesRepository = TestPreferencesRepository(
      uiPreferences = UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { ViewMode.GRID },
      ),
    ),
    navigationProvider = emptyList(),
  )

  ProvideScenePeekAppState(appState = appState) {
    SharedTransitionScopeProvider { scope ->
      scope.PersonContent(
        uiState = uiState,
        lazyListState = lazyListState,
        scope = rememberCoroutineScope(),
        onMediaClick = {},
        onTabSelected = {},
        onSwitchPreferences = {},
        onApplyFilter = {},
        onProgressUpdate = {},
        onNavigate = {},
        visibilityScope = this,
      )
    }
  }
}

@Previews
@Composable
fun PersonContentGridPreview() {
  val appState = rememberScenePeekAppState(
    networkMonitor = TestNetworkMonitor(),
    onboardingManager = TestOnboardingManager(),
    preferencesRepository = TestPreferencesRepository(
      uiPreferences = UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { ViewMode.GRID },
      ),
    ),
    navigationProvider = emptyList(),
  )
  ProvideScenePeekAppState(appState = appState) {
    SharedTransitionScopeProvider { scope ->
      scope.PersonContent(
        uiState = PersonUiState(
          selectedTabIndex = 2,
          forms = mapOf(
            0 to PersonForm.About(
              PersonDetailsUiState.Data.Visible(PersonDetailsFactory.steveCarell()),
            ),
            1 to PersonForm.Movies(emptyMap()),
            2 to PersonForm.TvShows(GroupedPersonCreditsSample.tvShows()),
          ),
          filteredCredits = mapOf(
            2 to GroupedPersonCreditsSample.tvShows(),
          ),
          filters = mapOf(
            1 to emptyList(),
            2 to emptyList(),
          ),
          tabs = PersonTab.entries,
          knownForCredits = PersonCastCreditFactory.all(),
        ),
        lazyListState = rememberLazyListState(),
        scope = rememberCoroutineScope(),
        onMediaClick = {},
        onTabSelected = {},
        onSwitchPreferences = {},
        onApplyFilter = {},
        onProgressUpdate = {},
        onNavigate = {},
        visibilityScope = this,
      )
    }
  }
}
