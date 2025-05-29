package com.divinelink.feature.details.media.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.designsystem.theme.updateStatusBarColor
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
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
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.details.media.ui.collapsing.DynamicDetailsCollapsingToolbar
import com.divinelink.feature.details.media.ui.fab.DetailsExpandableFloatingActionButton
import com.divinelink.feature.details.media.ui.forms.about.AboutFormContent
import com.divinelink.feature.details.media.ui.forms.cast.CastFormContent
import com.divinelink.feature.details.media.ui.forms.recommendation.RecommendationsFormContent
import com.divinelink.feature.details.media.ui.forms.reviews.ReviewsFormContent
import com.divinelink.feature.details.media.ui.forms.seasons.SeasonsFormContent
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
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  onMarkAsFavoriteClicked: () -> Unit,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onPersonClick: (Person) -> Unit,
  onConsumeSnackbar: () -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  requestMedia: (List<Int>) -> Unit,
  onViewAllCreditsClick: () -> Unit,
  onObfuscateSpoilers: () -> Unit,
  onShowAllRatingsClick: () -> Unit,
  onTabSelected: (Int) -> Unit,
  onPlayTrailerClick: (String) -> Unit,
) {
  val view = LocalView.current
  val isDarkTheme = LocalDarkThemeProvider.current

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  var showDropdownMenu by remember { mutableStateOf(false) }
  var isAppBarVisible by remember { mutableStateOf(false) }
  var onBackdropLoaded by remember { mutableStateOf(false) }

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

  val containerColor by animateColorAsState(
    targetValue = when (isAppBarVisible) {
      true -> MaterialTheme.colorScheme.surface
      false -> Color.Transparent
    },
    animationSpec = tween(durationMillis = 0),
    label = "TopAppBar Container Color",
  )

  val textColor = when {
    // When app bar is visible, we want to contrast against the app bar background
    isAppBarVisible -> MaterialTheme.colorScheme.onSurface

    // When backdrop has loaded, determine color based on theme
    onBackdropLoaded -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.surface
    }

    // When backdrop hasn't loaded yet, use default text colors
    else -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.onSurface // Changed this to onSurface to ensure contrast
    }
  }

  val surfaceColor = MaterialTheme.colorScheme.surface
  DisposableEffect(textColor) {
    val isLight = textColor == surfaceColor
    updateStatusBarColor(view = view, setLight = !isLight && !isDarkTheme)

    onDispose {
      // Reset the status bar color when the composable is disposed
      updateStatusBarColor(view = view, setLight = !isDarkTheme)
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = animatedVisibilityScope,
  ).PersistentScaffold(
    modifier = Modifier
      .testTag(TestTags.Details.CONTENT_SCAFFOLD)
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      AppTopAppBar(
        modifier = Modifier.background(containerColor),
        scrollBehavior = scrollBehavior,
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = Color.Transparent,
          containerColor = Color.Transparent,
        ),
        contentColor = textColor,
        text = UIText.StringText(viewState.mediaDetails?.title ?: ""),
        isVisible = isAppBarVisible,
        actions = {
          FavoriteButton(
            modifier = Modifier.clip(MaterialTheme.shape.rounded),
            isFavorite = viewState.mediaDetails?.isFavorite ?: false,
            onClick = onMarkAsFavoriteClicked,
            inactiveColor = textColor,
            transparentBackground = true,
          )

          IconButton(
            modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
            onClick = { showDropdownMenu = !showDropdownMenu },
          ) {
            Icon(
              imageVector = Icons.Outlined.MoreVert,
              contentDescription = "More",
              tint = textColor,
            )
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
    floatingActionButton = {
      DetailsExpandableFloatingActionButton(
        actionButtons = viewState.actionButtons,
        onAddRateClicked = onAddRateClick,
        onAddToWatchlistClicked = onAddToWatchlistClick,
        onRequestClicked = { showRequestDialog = true },
      )
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        when (viewState.mediaDetails) {
          is Movie, is TV -> MediaDetailsContent(
            uiState = viewState,
            mediaDetails = viewState.mediaDetails,
            userDetails = viewState.userDetails,
            trailer = viewState.trailer,
            onMediaItemClick = onSimilarMovieClicked,
            onAddRateClick = onAddRateClick,
            onAddToWatchlistClick = onAddToWatchlistClick,
            viewAllCreditsClick = onViewAllCreditsClick,
            onPersonClick = onPersonClick,
            obfuscateEpisodes = viewState.spoilersObfuscated,
            ratingSource = viewState.ratingSource,
            viewAllRatingsClick = onShowAllRatingsClick,
            onTabSelected = onTabSelected,
            onWatchTrailer = onPlayTrailerClick,
            onShowTitle = { showTitle ->
              isAppBarVisible = showTitle
            },
            onBackdropLoaded = { onBackdropLoaded = true },
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
  uiState: DetailsViewState,
  ratingSource: RatingSource,
  mediaDetails: MediaDetails,
  userDetails: AccountMediaDetails?,
  trailer: Video?,
  obfuscateEpisodes: Boolean,
  onPersonClick: (Person) -> Unit,
  onMediaItemClick: (MediaItem.Media) -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  viewAllCreditsClick: () -> Unit,
  viewAllRatingsClick: () -> Unit,
  onWatchTrailer: (String) -> Unit,
  onTabSelected: (Int) -> Unit,
  onShowTitle: (Boolean) -> Unit,
  onBackdropLoaded: () -> Unit,
) {
  val scope = rememberCoroutineScope()

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

  DynamicDetailsCollapsingToolbar(
    mediaDetails = mediaDetails,
    ratingSource = ratingSource,
    hasTrailer = trailer?.key != null,
    onAddToWatchlistClick = onAddToWatchlistClick,
    onAddRateClick = onAddRateClick,
    onShowAllRatingsClick = viewAllRatingsClick,
    userDetails = userDetails,
    onShowTitle = onShowTitle,
    onWatchTrailerClick = { trailer?.key?.let { onWatchTrailer(it) } },
    onBackdropLoaded = onBackdropLoaded,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      ScenePeekTabs(
        tabs = uiState.tabs,
        selectedIndex = selectedPage,
        onClick = {
          scope.launch {
            pagerState.animateScrollToPage(it)
          }
        },
      )

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
            DetailsForm.Loading -> LoadingContent(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            )

            is DetailsForm.Content<*> -> when (form.data) {
              is DetailsData.About -> AboutFormContent(
                modifier = Modifier.fillMaxSize(),
                aboutData = form.data as DetailsData.About,
                onPersonClick = onPersonClick,
                onGenreClick = {},
              )
              is DetailsData.Cast -> CastFormContent(
                modifier = Modifier.fillMaxSize(),
                cast = form.data as DetailsData.Cast,
                title = mediaDetails.title,
                onPersonClick = onPersonClick,
                obfuscateSpoilers = obfuscateEpisodes,
                onViewAllClick = viewAllCreditsClick,
              )
              is DetailsData.Recommendations -> RecommendationsFormContent(
                modifier = Modifier.fillMaxSize(),
                recommendations = form.data as DetailsData.Recommendations,
                title = mediaDetails.title,
                onItemClick = onMediaItemClick,
              )
              is DetailsData.Reviews -> ReviewsFormContent(
                modifier = Modifier.fillMaxSize(),
                title = mediaDetails.title,
                reviews = form.data as DetailsData.Reviews,
              )
              is DetailsData.Seasons -> SeasonsFormContent(
                modifier = Modifier.fillMaxSize(),
                title = mediaDetails.title,
                reviews = form.data as DetailsData.Seasons,
              )
            }
          }
        }
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
  AnimatedVisibilityScopeProvider { _, visibilityScope ->
    ProvideSnackbarController(
      snackbarHostState = snackbarHostState,
      coroutineScope = coroutineScope,
    ) {
      AppTheme {
        Surface {
          DetailsContent(
            modifier = Modifier,
            viewState = viewState,
            animatedVisibilityScope = visibilityScope,
            onNavigateUp = {},
            onMarkAsFavoriteClicked = {},
            onSimilarMovieClicked = {},
            onConsumeSnackbar = {},
            onAddRateClick = {},
            onAddToWatchlistClick = {},
            requestMedia = {},
            onPersonClick = {},
            onViewAllCreditsClick = {},
            onObfuscateSpoilers = {},
            onShowAllRatingsClick = {},
            onTabSelected = {},
            onPlayTrailerClick = {},
          )
        }
      }
    }
  }
}
