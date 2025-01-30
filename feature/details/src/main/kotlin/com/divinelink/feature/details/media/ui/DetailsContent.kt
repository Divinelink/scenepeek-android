package com.divinelink.feature.details.media.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.DetailsDropdownMenu
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.WatchlistButton
import com.divinelink.core.ui.components.details.SpannableRating
import com.divinelink.core.ui.components.details.cast.CastList
import com.divinelink.core.ui.components.details.cast.CreatorsItem
import com.divinelink.core.ui.components.details.cast.DirectorItem
import com.divinelink.core.ui.components.details.genres.GenreLabel
import com.divinelink.core.ui.components.details.reviews.ReviewsList
import com.divinelink.core.ui.components.details.similar.SimilarMoviesList
import com.divinelink.core.ui.components.details.videos.VideoState
import com.divinelink.core.ui.components.details.videos.YoutubePlayer
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.components.dialog.RequestMovieDialog
import com.divinelink.core.ui.components.dialog.SelectSeasonsDialog
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.core.ui.rating.MediaRatingItem
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.feature.details.R
import com.divinelink.core.ui.R as uiR

private const val MAX_WIDTH_FOR_LANDSCAPE_PLAYER = 0.55f

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
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val listState = rememberLazyListState()
  var showDropdownMenu by remember { mutableStateOf(false) }
  val titleIsVisible = remember {
    derivedStateOf { listState.firstVisibleItemIndex != 0 }
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
        isVisible = titleIsVisible.value,
        actions = {
          FavoriteButton(
            modifier = Modifier.clip(MaterialTheme.shape.roundedShape),
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
      viewState.mediaDetails?.let { mediaDetails ->
        when (mediaDetails) {
          is Movie, is TV -> {
            MediaDetailsContent(
              modifier = Modifier.padding(paddingValues = paddingValues),
              listState = listState,
              mediaDetails = mediaDetails,
              userDetails = viewState.userDetails,
              tvCredits = viewState.tvCredits?.cast,
              similarMoviesList = viewState.similarMovies,
              reviewsList = viewState.reviews,
              trailer = viewState.trailer,
              onSimilarMovieClicked = onSimilarMovieClicked,
              onAddRateClicked = onAddRateClicked,
              onAddToWatchlistClicked = onAddToWatchlistClicked,
              viewAllCreditsClicked = viewAllCreditsClicked,
              onPersonClick = onPersonClick,
              obfuscateEpisodes = viewState.spoilersObfuscated,
              ratingSource = viewState.ratingSource,
              viewAllRatingsClicked = viewAllRatingsClicked,
            )
          }
        }
      }
      if (viewState.error != null) {
        SimpleAlertDialog(
          confirmClick = {
            onNavigateUp()
          },
          confirmText = UIText.ResourceText(uiR.string.core_ui_ok),
          uiState = AlertDialogUiState(text = viewState.error),
        )
      }
    },
  )
  if (viewState.isLoading) {
    LoadingContent()
  }
}

@Composable
private fun VideoPlayerSection(
  modifier: Modifier = Modifier,
  trailer: Video,
  onVideoStateChange: (VideoState) -> Unit,
) {
  val orientation = LocalConfiguration.current.orientation
  val playerWidth = remember {
    derivedStateOf {
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        MAX_WIDTH_FOR_LANDSCAPE_PLAYER
      } else {
        1f
      }
    }
  }

  when (trailer.site) {
    VideoSite.YouTube ->
      YoutubePlayer(
        modifier = modifier
          .fillMaxWidth(playerWidth.value),
        video = trailer,
        onStateChange = { state ->
          onVideoStateChange(state)
        },
      )

    else -> {
      return
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaDetailsContent(
  modifier: Modifier = Modifier,
  listState: LazyListState,
  ratingSource: RatingSource,
  mediaDetails: MediaDetails,
  tvCredits: List<Person>?,
  userDetails: AccountMediaDetails?,
  similarMoviesList: List<MediaItem.Media>?,
  reviewsList: List<Review>?,
  trailer: Video?,
  obfuscateEpisodes: Boolean,
  onPersonClick: (Person) -> Unit,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
  viewAllCreditsClicked: () -> Unit,
  viewAllRatingsClicked: () -> Unit,
) {
  val showStickyPlayer = remember { mutableStateOf(false) }

  ScenePeekLazyColumn(
    modifier = modifier
      .testTag(TestTags.Details.CONTENT_LIST)
      .fillMaxWidth(),
    state = listState,
  ) {
    item {
      TitleDetails(mediaDetails)
    }
    if (trailer != null) {
      stickyHeader(key = "trailerSticky") {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        ) {
          VideoPlayerSection(
            modifier = Modifier,
            trailer = trailer,
            onVideoStateChange = { state ->
              showStickyPlayer.value = state == VideoState.PLAYING
            },
          )
        }
      }

      if (!showStickyPlayer.value) {
        stickyHeader {
          Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_0))
        }
      }
    }

    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(paddingValues = ListPaddingValues),
      ) {
        MovieImage(
          modifier = Modifier.weight(1f),
          path = mediaDetails.posterPath,
        )

        OverviewDetails(
          modifier = Modifier.weight(OVERVIEW_WEIGHT),
          movieDetails = mediaDetails,
          genres = mediaDetails.genres,
          onGenreClicked = {},
        )
      }
    }

    item {
      WatchlistButton(
        modifier = Modifier.padding(paddingValues = ListPaddingValues),
        onWatchlist = userDetails?.watchlist == true,
        onClick = onAddToWatchlistClicked,
      )
    }

    item {
      UserRating(
        ratingDetails = mediaDetails.ratingCount.getRatingDetails(ratingSource),
        accountRating = userDetails?.beautifiedRating,
        onAddRateClicked = onAddRateClicked,
        onShowAllRatingsClicked = viewAllRatingsClicked,
        source = ratingSource,
      )
    }

    item {
      if (mediaDetails is TV && tvCredits != null) {
        HorizontalDivider(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
          thickness = MaterialTheme.dimensions.keyline_1,
        )
        CastList(
          cast = tvCredits.take(30),
          onViewAllClick = viewAllCreditsClicked,
          onPersonClick = onPersonClick,
          obfuscateEpisodes = obfuscateEpisodes,
        ) // This is temporary
        CreatorsItem(
          creators = mediaDetails.creators,
          onClick = onPersonClick,
        )
      } else if (mediaDetails is Movie) {
        HorizontalDivider(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
          thickness = MaterialTheme.dimensions.keyline_1,
        )
        CastList(
          cast = mediaDetails.cast,
          onViewAllClick = viewAllCreditsClicked,
          viewAllVisible = false,
          onPersonClick = onPersonClick,
        )
        mediaDetails.director?.let {
          DirectorItem(director = it, onClick = onPersonClick)
        }
      }
      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))
    }
    if (similarMoviesList?.isNotEmpty() == true) {
      item {
        HorizontalDivider(thickness = MaterialTheme.dimensions.keyline_1)
        SimilarMoviesList(
          movies = similarMoviesList,
          onSimilarMovieClicked = onSimilarMovieClicked,
        )
      }
    }

    if (!reviewsList.isNullOrEmpty()) {
      item {
        HorizontalDivider(thickness = MaterialTheme.dimensions.keyline_1)
        ReviewsList(
          reviews = reviewsList,
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
    }
  }
}

@Composable
private fun UserRating(
  modifier: Modifier = Modifier,
  source: RatingSource,
  ratingDetails: RatingDetails,
  accountRating: Int?,
  onAddRateClicked: () -> Unit,
  onShowAllRatingsClicked: () -> Unit,
) {
  Row(
    modifier = modifier.fillMaxSize(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_12),
    )

    TextButton(
      modifier = Modifier.testTag(TestTags.Rating.DETAILS_RATING_BUTTON),
      onClick = onShowAllRatingsClicked,
    ) {
      MediaRatingItem(
        ratingDetails = ratingDetails,
        source = source,
      )
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_0),
    )

    TextButton(
      modifier = Modifier.align(Alignment.CenterVertically),
      onClick = onAddRateClicked,
    ) {
      if (accountRating != null) {
        SpannableRating(
          modifier = Modifier.align(Alignment.CenterVertically),
          text = stringResource(id = R.string.details__your_rating),
          rating = accountRating,
          vertical = true,
        )
      } else {
        Text(
          text = stringResource(id = R.string.details__add_rating),
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_24),
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TitleDetails(mediaDetails: MediaDetails) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MaterialTheme.dimensions.keyline_12)
      .padding(bottom = MaterialTheme.dimensions.keyline_12),
  ) {
    Text(
      style = MaterialTheme.typography.displaySmall,
      text = mediaDetails.title,
    )

    FlowRow {
      Text(
        style = MaterialTheme.typography.labelMedium,
        text = mediaDetails.releaseDate,
      )

      when (mediaDetails) {
        is Movie -> mediaDetails.runtime?.let { runtime ->
          Text(
            style = MaterialTheme.typography.labelMedium,
            text = " • $runtime",
          )
        }
        is TV -> {
          if (mediaDetails.status != TvStatus.UNKNOWN) {
            Text(
              style = MaterialTheme.typography.labelMedium,
              text = " • " + stringResource(mediaDetails.status.resId),
            )
          }

          if (mediaDetails.numberOfSeasons > 0) {
            Text(
              style = MaterialTheme.typography.labelMedium,
              text = " • " + pluralStringResource(
                id = R.plurals.feature_details_number_of_seasons,
                count = mediaDetails.numberOfSeasons,
                mediaDetails.numberOfSeasons,
              ),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun OverviewDetails(
  modifier: Modifier = Modifier,
  movieDetails: MediaDetails,
  genres: List<String>?,
  onGenreClicked: (String) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(start = MaterialTheme.dimensions.keyline_12)
      .fillMaxWidth(),
  ) {
    if (movieDetails.genres?.isNotEmpty() == true) {
      genres?.let {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        ) {
          items(genres) { genre ->
            GenreLabel(
              genre = genre,
              onGenreClicked = { onGenreClicked(genre) },
            )
          }
        }
      }
    }
    if (!movieDetails.overview.isNullOrEmpty()) {
      Text(
        modifier = Modifier.padding(
          top = MaterialTheme.dimensions.keyline_16,
          bottom = MaterialTheme.dimensions.keyline_8,
        ),
        text = movieDetails.overview!!,
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}

private const val OVERVIEW_WEIGHT = 3f

@Previews
@Composable
fun DetailsContentPreview(
  @PreviewParameter(DetailsViewStateProvider::class)
  viewState: DetailsViewState,
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
        )
      }
    }
  }
}

@Suppress("MagicNumber")
@ExcludeFromKoverReport
class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {
  override val values: Sequence<DetailsViewState>
    get() {
      val reviews = (1..2).map {
        Review(
          authorName = "Author name $it",
          rating = 10,
          content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales " +
            "laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu" +
            "elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat" +
            "risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante" +
            "nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam" +
            "neque. Praesent ut lacus sed justo viverra fermentum et ut sem. \n Fusce" +
            "convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia." +
            "Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum" +
            "ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis" +
            "purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.",
          date = "2022-10-22",
        )
      }
      val similarMovies = (1..10).map {
        MediaItem.Media.Movie(
          id = it,
          posterPath = "",
          releaseDate = "",
          name = "Flight Club",
          voteAverage = 7.2,
          voteCount = 1020,
          overview = "This movie is good.",
          isFavorite = false,
        )
      }.toList()
      val popularMovie = MediaItem.Media.Movie(
        id = 0,
        posterPath = "",
        releaseDate = "",
        name = "Flight Club",
        voteAverage = 7.2,
        voteCount = 1_453_020,
        overview = "This movie is good.",
        isFavorite = false,
      )

      return sequenceOf(
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          tvCredits = null,
          isLoading = true,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          userDetails = AccountMediaDetails(
            id = 8679,
            favorite = false,
            rating = 9.0f,
            watchlist = false,
          ),
          mediaType = MediaType.MOVIE,
          tvCredits = null,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tvCredits = null,
          similarMovies = similarMovies,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            numberOfSeasons = 0,
          ),
          tvCredits = null,
          similarMovies = similarMovies,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            status = TvStatus.UNKNOWN,
          ),
          tvCredits = null,
          similarMovies = similarMovies,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          similarMovies = similarMovies,
          tvCredits = null,
          reviews = reviews,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          similarMovies = similarMovies,
          userDetails = AccountMediaDetails(
            id = 0,
            favorite = false,
            rating = 9.0f,
            watchlist = true,
          ),
          tvCredits = null,
          reviews = reviews,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          tvCredits = null,
          error = UIText.StringText("Something went wrong."),
        ),
      )
    }
}
