package com.andreolas.movierama.details.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.components.WatchlistButton
import com.andreolas.movierama.ui.components.details.SpannableRating
import com.andreolas.movierama.ui.components.details.cast.CastList
import com.andreolas.movierama.ui.components.details.genres.GenreLabel
import com.andreolas.movierama.ui.components.details.reviews.ReviewsList
import com.andreolas.movierama.ui.components.details.similar.SimilarMoviesList
import com.andreolas.movierama.ui.components.details.videos.VideoState
import com.andreolas.movierama.ui.components.details.videos.YoutubePlayer
import com.andreolas.movierama.ui.components.dialog.AlertDialogUiState
import com.andreolas.movierama.ui.components.dialog.SimpleAlertDialog
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.crew.Actor
import com.divinelink.core.model.details.crew.Director
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.ui.FavoriteButton
import com.divinelink.ui.MediaRatingItem
import com.divinelink.ui.MovieImage
import com.divinelink.ui.RatingSize
import com.divinelink.ui.UIText
import com.divinelink.ui.snackbar.SnackbarMessageHandler
import com.divinelink.ui.snackbar.controller.ProvideSnackbarController

const val MOVIE_DETAILS_SCROLLABLE_LIST_TAG = "MOVIE_DETAILS_LAZY_COLUMN_TAG"
private const val MAX_WIDTH_FOR_LANDSCAPE_PLAYER = 0.55f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
  viewState: DetailsViewState,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit,
  onMarkAsFavoriteClicked: () -> Unit,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onConsumeSnackbar: () -> Unit,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
  showOrHideShareDialog: (Boolean) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  var showOverflowMenu by remember { mutableStateOf(false) }

  if (viewState.openShareDialog) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, viewState.shareUrl)
    }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share via"))
    showOrHideShareDialog(false)
  }

  SnackbarMessageHandler(
    snackbarMessage = viewState.snackbarMessage,
    onDismissSnackbar = onConsumeSnackbar
  )

  Scaffold(
    modifier = modifier
      .navigationBarsPadding()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
          Text(
            text = viewState.mediaDetails?.title ?: "",
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateUp,
          ) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              stringResource(R.string.navigate_up_button_content_description)
            )
          }
        },
        actions = {
          FavoriteButton(
            modifier = Modifier.clip(MaterialTheme.shape.roundedShape),
            isFavorite = viewState.mediaDetails?.isFavorite ?: false,
            onClick = onMarkAsFavoriteClicked,
            inactiveColor = MaterialTheme.colorScheme.onSurface,
          )

          IconButton(
            modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
            onClick = { showOverflowMenu = !showOverflowMenu }) {
            Icon(Icons.Outlined.MoreVert, "More")
          }

          DropdownMenu(
            modifier = Modifier
              .widthIn(min = 180.dp)
              .testTag(TestTags.Menu.DROPDOWN_MENU),
            expanded = showOverflowMenu,
            onDismissRequest = { showOverflowMenu = false }
          ) {

            DropdownMenuItem(
              modifier = Modifier.testTag(
                TestTags.Menu.MENU_ITEM.format(stringResource(id = R.string.share))
              ),
              text = {
                Text(text = stringResource(id = R.string.share))
              },
              onClick = {
                showOverflowMenu = false
                showOrHideShareDialog(true)
              },
            )
          }
        }
      )
    },
    content = { paddingValues ->
      viewState.mediaDetails?.let { mediaDetails ->
        when (mediaDetails) {
          is Movie, is TV -> {
            MediaDetailsContent(
              modifier = Modifier.padding(paddingValues = paddingValues),
              mediaDetails = mediaDetails,
              userDetails = viewState.userDetails,
              similarMoviesList = viewState.similarMovies,
              reviewsList = viewState.reviews,
              trailer = viewState.trailer,
              onSimilarMovieClicked = onSimilarMovieClicked,
              onAddRateClicked = onAddRateClicked,
              onAddToWatchlistClicked = onAddToWatchlistClicked
            )
          }
        }
      }
      if (viewState.error != null) {
        SimpleAlertDialog(
          confirmClick = {
            onNavigateUp()
          },
          confirmText = UIText.ResourceText(R.string.ok),
          uiState = AlertDialogUiState(text = viewState.error)
        )
      }

      if (viewState.isLoading) {
        LoadingContent()
      }
    }
  )
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
  mediaDetails: MediaDetails,
  userDetails: AccountMediaDetails?,
  similarMoviesList: List<MediaItem.Media>?,
  reviewsList: List<Review>?,
  trailer: Video?,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onAddRateClicked: () -> Unit,
  onAddToWatchlistClicked: () -> Unit,
) {
  val showStickyPlayer = remember { mutableStateOf(false) }

  Surface {
    LazyColumn(
      modifier = modifier
        .testTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
        .fillMaxWidth()
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
          onClick = onAddToWatchlistClicked
        )
      }

      item {
        UserRating(
          overallUserScore = mediaDetails.rating,
          userRating = userDetails?.beautifiedRating,
          onAddRateClicked = onAddRateClicked
        )
      }

      item {
        HorizontalDivider(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
          thickness = MaterialTheme.dimensions.keyline_1
        )
        CastList(
          cast = mediaDetails.cast,
          director = mediaDetails.director,
        )
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
}

@Composable
private fun UserRating(
  modifier: Modifier = Modifier,
  overallUserScore: String,
  userRating: String?,
  onAddRateClicked: () -> Unit,
) {
  Row(
    modifier = modifier.fillMaxSize(),
    verticalAlignment = Alignment.CenterVertically,
  ) {

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_24)
    )

    Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      MediaRatingItem(
        rating = overallUserScore,
        size = RatingSize.LARGE
      )
      Text(
        text = stringResource(id = R.string.details__user_score),
        style = MaterialTheme.typography.titleMedium,
      )
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_24)
    )

    TextButton(
      modifier = Modifier.align(Alignment.CenterVertically),
      onClick = onAddRateClicked
    ) {
      if (userRating != null) {
        SpannableRating(
          modifier = Modifier.align(Alignment.CenterVertically),
          text = stringResource(id = R.string.details__your_rating),
          rating = userRating,
          newLine = true,
        )
      } else {
        Text(
          text = stringResource(id = R.string.details__add_rating),
          style = MaterialTheme.typography.titleMedium
        )
      }
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_24)
    )
  }
}

@Composable
private fun TitleDetails(mediaDetails: MediaDetails) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MaterialTheme.dimensions.keyline_12)
  ) {
    Text(
      modifier = Modifier.weight(1f),
      style = MaterialTheme.typography.displaySmall,
      text = mediaDetails.title,
    )
  }

  Row(
    modifier = Modifier
      .padding(
        start = MaterialTheme.dimensions.keyline_16,
        end = MaterialTheme.dimensions.keyline_12,
        bottom = MaterialTheme.dimensions.keyline_16
      ),
  ) {
    Text(
      style = MaterialTheme.typography.bodySmall,
      text = mediaDetails.releaseDate,
    )
    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_12))

    if (mediaDetails is Movie) {
      mediaDetails.runtime?.let { runtime ->
        Text(
          style = MaterialTheme.typography.bodySmall,
          text = runtime,
        )
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
              onGenreClicked = { onGenreClicked(genre) }
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

@Preview(
  name = "Night Mode",
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
  name = "Day Mode",
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
@ExcludeFromKoverReport
private fun DetailsContentPreview(
  @PreviewParameter(DetailsViewStateProvider::class)
  viewState: DetailsViewState,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope
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
          showOrHideShareDialog = {},
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
          date = "2022-10-22"
        )
      }
      val similarMovies = (1..10).map {
        MediaItem.Media.Movie(
          id = it,
          posterPath = "",
          releaseDate = "",
          name = "Flight Club",
          rating = "",
          overview = "This movie is good.",
          isFavorite = false,
        )
      }.toList()
      val popularMovie = MediaItem.Media.Movie(
        id = 0,
        posterPath = "",
        releaseDate = "",
        name = "Flight Club",
        rating = "",
        overview = "This movie is good.",
        isFavorite = false,
      )
      val movieDetails = Movie(
        id = 1123,
        posterPath = "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "9.5",
        isFavorite = false,
        overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal" +
          " male aggression into a shocking new form of therapy. Their concept catches on," +
          " with underground fight clubs forming in every town, until an eccentric gets in the" +
          " way and ignites an out-of-control spiral toward oblivion.",
        director = Director(
          id = 123443321,
          name = "Forest Gump",
          profilePath = "BoxOfChocolates.jpg"
        ),
        cast = listOf(
          Actor(
            id = 1,
            name = "Jack",
            profilePath = "AllWorkAndNoPlay.jpg",
            character = "HelloJohnny",
            order = 0
          ),
          Actor(
            id = 2,
            name = "Nicholson",
            profilePath = "Cuckoo.jpg",
            character = "McMurphy",
            order = 1
          ),
          Actor(
            id = 3,
            name = "Jack",
            profilePath = "AllWorkAndNoPlay.jpg",
            character = "HelloJohnny",
            order = 0
          ),
          Actor(
            id = 4,
            name = "Nicholson",
            profilePath = "Cuckoo.jpg",
            character = "McMurphy",
            order = 1
          ),
        ),
        genres = listOf("Thriller", "Drama", "Comedy", "Mystery", "Fantasy"),
        runtime = "2h 10m",
      )

      return sequenceOf(
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          isLoading = true,
        ),

        DetailsViewState(
          mediaId = popularMovie.id,
          userDetails = AccountMediaDetails(
            id = 8679,
            favorite = false,
            rating = 9.0f,
            watchlist = false
          ),
          mediaType = MediaType.MOVIE,
          mediaDetails = movieDetails,
        ),

        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          mediaDetails = movieDetails,
          similarMovies = similarMovies,
        ),

        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          mediaDetails = movieDetails,
          similarMovies = similarMovies,
          reviews = reviews,
        ),

        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          mediaDetails = movieDetails,
          similarMovies = similarMovies,
          userDetails = AccountMediaDetails(
            id = 0,
            favorite = false,
            rating = 9.0f,
            watchlist = true
          ),
          reviews = reviews,
        ),

        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          error = UIText.StringText("Something went wrong.")
        ),
      )
    }
}
