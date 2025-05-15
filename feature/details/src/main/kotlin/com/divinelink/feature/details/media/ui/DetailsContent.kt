package com.divinelink.feature.details.media.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailsDropdownMenu
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.components.dialog.RequestMovieDialog
import com.divinelink.core.ui.components.dialog.SelectSeasonsDialog
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.core.ui.nestedscroll.rememberCollapsingContentNestedScrollConnection
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.details.media.DetailsData
import com.divinelink.feature.details.media.DetailsForm
import com.divinelink.feature.details.media.ui.components.CollapsibleDetailsContent
import com.divinelink.feature.details.media.ui.forms.about.AboutFormContent
import com.divinelink.feature.details.media.ui.forms.cast.CastFormContent
import com.divinelink.feature.details.media.ui.forms.recommendation.RecommendationsFormContent
import com.divinelink.feature.details.media.ui.forms.reviews.ReviewsFormContent
import com.divinelink.feature.details.media.ui.provider.DetailsViewStateProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
  viewState: DetailsViewState,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit,
  onMarkAsFavoriteClicked: () -> Unit,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onPersonClick: (Person) -> Unit,
  onConsumeSnackbar: () -> Unit,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
  requestMedia: (List<Int>) -> Unit,
  viewAllCreditsClicked: () -> Unit,
  onObfuscateSpoilers: () -> Unit,
  viewAllRatingsClicked: () -> Unit,
  onTabSelected: (Int) -> Unit,
) {
  val connection = rememberCollapsingContentNestedScrollConnection()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val listState = rememberLazyListState()
  var showDropdownMenu by remember { mutableStateOf(false) }

  val isAppBarVisible by remember {
    derivedStateOf { connection.currentSize < 152.dp }
  }

  SnackbarMessageHandler(
    snackbarMessage = viewState.snackbarMessage,
    onDismissSnackbar = onConsumeSnackbar,
  )

  var showRequestDialog by remember { mutableStateOf(false) }
  if (showRequestDialog) {
    when (viewState.mediaDetails) {
      is TV -> SelectSeasonsDialog(
        numberOfSeasons = viewState.mediaDetails.numberOfSeasons,
        onRequestClick = {
          requestMedia(it)
          showRequestDialog = false
        },
        onDismissRequest = { showRequestDialog = false },
      )
      is Movie -> RequestMovieDialog(
        onDismissRequest = { showRequestDialog = false },
        onConfirm = {
          requestMedia(emptyList())
          showRequestDialog = false
        },
        title = viewState.mediaDetails.title,
      )
      null -> {
        // Do nothing
      }
    }
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
    modifier = modifier
      .testTag(TestTags.Details.CONTENT_SCAFFOLD)
      .navigationBarsPadding()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    floatingActionButton = {
      DetailsExpandableFloatingActionButton(
        actionButtons = viewState.actionButtons,
        onAddRateClicked = onAddRateClicked,
        onAddToWatchlistClicked = onAddToWatchlistClicked,
        onRequestClicked = { showRequestDialog = true },
      )
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        text = UIText.StringText(viewState.mediaDetails?.title ?: ""),
        isVisible = isAppBarVisible,
        actions = {
          FavoriteButton(
            modifier = Modifier.clip(MaterialTheme.shape.rounded),
            isFavorite = viewState.mediaDetails?.isFavorite ?: false,
            onClick = onMarkAsFavoriteClicked,
            inactiveColor = MaterialTheme.colorScheme.onSurface,
          )

          IconButton(
            modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
            onClick = { showDropdownMenu = !showDropdownMenu },
          ) {
            Icon(Icons.Outlined.MoreVert, "More")
          }

          viewState.mediaDetails?.let {
            DetailsDropdownMenu(
              mediaDetails = viewState.mediaDetails,
              expanded = showDropdownMenu,
              options = viewState.menuOptions,
              spoilersObfuscated = viewState.spoilersObfuscated,
              onDismissDropdown = { showDropdownMenu = false },
              onObfuscateClick = onObfuscateSpoilers,
            )
          }
        },
        onNavigateUp = onNavigateUp,
      )
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        when (viewState.mediaDetails) {
          is Movie, is TV -> MediaDetailsContent(
            connection = connection,
            listState = listState,
            uiState = viewState,
            mediaDetails = viewState.mediaDetails,
            userDetails = viewState.userDetails,
            trailer = viewState.trailer,
            onMediaItemClick = onSimilarMovieClicked,
            onAddRateClicked = onAddRateClicked,
            onAddToWatchlistClicked = onAddToWatchlistClicked,
            viewAllCreditsClick = viewAllCreditsClicked,
            onPersonClick = onPersonClick,
            obfuscateEpisodes = viewState.spoilersObfuscated,
            ratingSource = viewState.ratingSource,
            viewAllRatingsClicked = viewAllRatingsClicked,
            onSizeChange = { connection.setMaxHeight(it.toFloat()) },
            onTabSelected = onTabSelected,
          )
          null -> {
            // Do nothing
          }
        }
        if (viewState.error != null) {
          SimpleAlertDialog(
            confirmClick = onNavigateUp,
            confirmText = UIText.ResourceText(uiR.string.core_ui_ok),
            uiState = AlertDialogUiState(text = viewState.error),
          )
        }
      }
    },
  )
  if (viewState.isLoading) {
    LoadingContent()
  }
}

@Composable
private fun MediaDetailsContent(
  modifier: Modifier = Modifier,
  listState: LazyListState,
  uiState: DetailsViewState,
  connection: CollapsingContentNestedScrollConnection,
  ratingSource: RatingSource,
  mediaDetails: MediaDetails,
  userDetails: AccountMediaDetails?,
  trailer: Video?,
  obfuscateEpisodes: Boolean,
  onPersonClick: (Person) -> Unit,
  onMediaItemClick: (MediaItem.Media) -> Unit,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
  viewAllCreditsClick: () -> Unit,
  viewAllRatingsClicked: () -> Unit,
  onSizeChange: (Int) -> Unit,
  onTabSelected: (Int) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val showStickyPlayer = remember { mutableStateOf(false) }

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTabIndex) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        onTabSelected(page)
      }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .nestedScroll(connection),
  ) {
    CollapsibleDetailsContent(
      modifier = Modifier
        .fillMaxWidth()
        .onSizeChanged { onSizeChange(it.height) },
      connection = connection,
      mediaDetails = mediaDetails,
      isOnWatchlist = userDetails?.watchlist == true,
      userDetails = userDetails,
      ratingSource = ratingSource,
      ratingCount = mediaDetails.ratingCount,
      onAddToWatchListClick = onAddToWatchlistClicked,
      onAddRateClick = onAddRateClicked,
      onShowAllRatingsClick = viewAllRatingsClicked,
    )

    ScenePeekLazyColumn(
      modifier = modifier
        .fillMaxSize()
        .offset { IntOffset(0, connection.currentSize.roundToPx()) }
        .testTag(TestTags.Details.CONTENT_LIST),
      state = listState,
    ) {
//      if (trailer != null) {
//        stickyHeader(key = "trailerSticky") {
//          Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//              .fillMaxWidth()
//              .background(Color.Black),
//          ) {
//            VideoPlayerSection(
//              modifier = Modifier,
//              trailer = trailer,
//              onVideoStateChange = { state ->
//                showStickyPlayer.value = state == VideoState.PLAYING
//              },
//            )
//          }
//        }
//
//        if (!showStickyPlayer.value) {
//          stickyHeader {
//            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_0))
//          }
//        }
//      }

      stickyHeader {
        ScenePeekTabs(
          tabs = uiState.tabs,
          selectedIndex = selectedPage,
          onClick = {
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
              DetailsForm.Error -> {
                // TODO("Handle error state")
              }
              DetailsForm.Loading -> Column(modifier = Modifier.fillParentMaxSize()) {
                LoadingContent(
                  modifier = Modifier
                    .weight(1.35f)
                    .padding(top = MaterialTheme.dimensions.keyline_24),
                )

                Spacer(modifier = Modifier.weight(5f))
              }
              is DetailsForm.Content<*> -> when (form.data) {
                is DetailsData.About -> AboutFormContent(
                  modifier = Modifier.fillParentMaxSize(),
                  aboutData = form.data,
                  onPersonClick = onPersonClick,
                  onGenreClick = {},
                )
                is DetailsData.TVAbout -> {
                }
                is DetailsData.Cast -> CastFormContent(
                  modifier = Modifier.fillParentMaxSize(),
                  cast = form.data,
                  title = mediaDetails.title,
                  onPersonClick = onPersonClick,
                  obfuscateSpoilers = obfuscateEpisodes,
                  onViewAllClick = viewAllCreditsClick,
                )
                is DetailsData.Recommendations -> RecommendationsFormContent(
                  modifier = Modifier.fillParentMaxSize(),
                  recommendations = form.data,
                  title = mediaDetails.title,
                  onItemClick = onMediaItemClick,
                )
                is DetailsData.Reviews -> ReviewsFormContent(
                  modifier = Modifier.fillParentMaxSize(),
                  title = mediaDetails.title,
                  reviews = form.data,
                )
              }
            }
          }
        }
      }

//      item {
//        if (mediaDetails is TV && tvCredits != null) {
//          HorizontalDivider(
//            modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
//            thickness = MaterialTheme.dimensions.keyline_1,
//          )
//          CastList(
//            cast = tvCredits.take(30),
//            onViewAllClick = viewAllCreditsClick,
//            onPersonClick = onPersonClick,
//            obfuscateEpisodes = obfuscateEpisodes,
//          ) // This is temporary
//          CreatorsItem(
//            creators = mediaDetails.creators,
//            onClick = onPersonClick,
//          )
//        } else if (mediaDetails is Movie) {
//          HorizontalDivider(
//            modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
//            thickness = MaterialTheme.dimensions.keyline_1,
//          )
//          CastList(
//            cast = mediaDetails.cast,
//            onViewAllClick = viewAllCreditsClick,
//            viewAllVisible = false,
//            onPersonClick = onPersonClick,
//          )
//          mediaDetails.director?.let {
//            DirectorItem(director = it, onClick = onPersonClick)
//          }
//        }
//        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))
//      }
//      if (similarMoviesList?.isNotEmpty() == true) {
//        item {
//          HorizontalDivider(thickness = MaterialTheme.dimensions.keyline_1)
//          SimilarMoviesList(
//            movies = similarMoviesList,
//            onMediaItemClick = onMediaItemClick,
//          )
//        }
//      }

//      if (!reviewsList.isNullOrEmpty()) {
//        item {
//          HorizontalDivider(thickness = MaterialTheme.dimensions.keyline_1)
//          ReviewsList(
//            reviews = reviewsList,
//          )
//        }
//      }

      item {
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
      }
    }
  }
}

@Previews
@Composable
fun DetailsContentPreview(
  @PreviewParameter(DetailsViewStateProvider::class) viewState: DetailsViewState,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope,
  ) {
    AppTheme {
      Surface {
        DetailsContent(
          modifier = Modifier,
          viewState = viewState,
          onNavigateUp = {},
          onMarkAsFavoriteClicked = {},
          onSimilarMovieClicked = {},
          onConsumeSnackbar = {},
          onAddRateClicked = {},
          onAddToWatchlistClicked = {},
          requestMedia = {},
          onPersonClick = {},
          viewAllCreditsClicked = {},
          onObfuscateSpoilers = {},
          viewAllRatingsClicked = {},
          onTabSelected = {},
        )
      }
    }
  }
}
