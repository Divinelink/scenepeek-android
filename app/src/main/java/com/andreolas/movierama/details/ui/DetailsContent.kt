@file:Suppress("LongMethod")

package com.andreolas.movierama.details.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.TVDetails
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.VideoSite
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.LikeButton
import com.andreolas.movierama.ui.components.MovieImage
import com.andreolas.movierama.ui.components.SimpleAlertDialog
import com.andreolas.movierama.ui.components.details.cast.CastList
import com.andreolas.movierama.ui.components.details.genres.GenreLabel
import com.andreolas.movierama.ui.components.details.reviews.ReviewsList
import com.andreolas.movierama.ui.components.details.similar.SimilarMoviesList
import com.andreolas.movierama.ui.components.details.videos.VideoState
import com.andreolas.movierama.ui.components.details.videos.YoutubePlayer
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.ListPaddingValues
import com.andreolas.movierama.ui.theme.MovieImageShape
import com.andreolas.movierama.ui.theme.RoundedShape
import com.andreolas.movierama.ui.theme.dimensions

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
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val scaffoldState = rememberScaffoldState()

  Scaffold(
    scaffoldState = scaffoldState,
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
            text = viewState.movieDetails?.title ?: "",
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
              Icons.Filled.ArrowBack,
              stringResource(R.string.navigate_up_button_content_description)
            )
          }
        },
        actions = {
          LikeButton(
            modifier = Modifier
              .padding(end = MaterialTheme.dimensions.keyline_8)
              .clip(RoundedShape),
            isFavorite = viewState.movieDetails?.isFavorite ?: false,
            onClick = onMarkAsFavoriteClicked,
          )
        }
      )
    },
    content = { paddingValues ->
      viewState.movieDetails?.let { mediaDetails ->
        when (mediaDetails) {
          is MovieDetails -> {
            DetailsMovieContent(
              modifier = Modifier.padding(paddingValues = paddingValues),
              movieDetails = mediaDetails,
              similarMoviesList = viewState.similarMovies,
              reviewsList = viewState.reviews,
              trailer = viewState.trailer,
              onSimilarMovieClicked = onSimilarMovieClicked,
            )
          }
          is TVDetails -> {
            // TODO Add TV Details Content
          }
        }
      }
      if (viewState.error != null) {
        SimpleAlertDialog(
          confirmClick = {
            onNavigateUp()
          },
          confirmText = UIText.ResourceText(R.string.ok),
          text = viewState.error,
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
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) MAX_WIDTH_FOR_LANDSCAPE_PLAYER else 1f
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
fun DetailsMovieContent(
  modifier: Modifier = Modifier,
  movieDetails: MovieDetails,
  similarMoviesList: List<MediaItem.Media>?,
  reviewsList: List<Review>?,
  trailer: Video?,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
) {
  val showStickyPlayer = remember { mutableStateOf(false) }

  Surface {
    LazyColumn(
      modifier = modifier
        .testTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
        .fillMaxWidth()
    ) {
      item {
        TitleDetails(movieDetails)
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
            modifier = Modifier
              .clip(MovieImageShape)
              .weight(1f),
            path = movieDetails.posterPath,
          )

          OverviewDetails(
            modifier = modifier.weight(OVERVIEW_WEIGHT),
            movieDetails = movieDetails,
            genres = movieDetails.genres,
            onGenreClicked = {},
          )
        }
      }
      item {
        Divider(thickness = MaterialTheme.dimensions.keyline_1)
        CastList(
          cast = movieDetails.cast,
          director = movieDetails.director,
        )
      }
      if (similarMoviesList?.isNotEmpty() == true) {
        item {
          Divider(thickness = MaterialTheme.dimensions.keyline_1)
          SimilarMoviesList(
            movies = similarMoviesList,
            onSimilarMovieClicked = onSimilarMovieClicked,
          )
        }
      }

      if (!reviewsList.isNullOrEmpty()) {
        item {
          Divider(thickness = MaterialTheme.dimensions.keyline_1)
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
private fun TitleDetails(movieDetails: MovieDetails) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MaterialTheme.dimensions.keyline_12)
  ) {
    Text(
      modifier = Modifier
        .weight(1f),
      style = MaterialTheme.typography.displaySmall,
      text = movieDetails.title,
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
      text = movieDetails.releaseDate,
    )
    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_12))
    movieDetails.runtime?.let {
      Text(
        style = MaterialTheme.typography.bodySmall,
        text = movieDetails.runtime,
      )
    }
  }
}

@Composable
private fun OverviewDetails(
  modifier: Modifier = Modifier,
  movieDetails: MovieDetails,
  genres: List<String>?,
  onGenreClicked: (String) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(start = MaterialTheme.dimensions.keyline_12)
      .fillMaxWidth(),
  ) {
    if (!movieDetails.genres.isNullOrEmpty()) {
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
    if (movieDetails.overview?.isNotEmpty() == true) {
      Text(
        modifier = Modifier.padding(
          top = MaterialTheme.dimensions.keyline_16,
          bottom = MaterialTheme.dimensions.keyline_8,
        ),
        text = movieDetails.overview,
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
@Suppress("UnusedPrivateMember")
@ExcludeFromJacocoGeneratedReport
private fun DetailsContentPreview(
  @PreviewParameter(DetailsViewStateProvider::class)
  viewState: DetailsViewState,
) {
  AppTheme {
    Surface {
      DetailsContent(
        modifier = Modifier,
        viewState = viewState,
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
      )
    }
  }
}

@Suppress("MagicNumber")
@ExcludeFromJacocoGeneratedReport
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
      val popularMovie = PopularMovie(
        id = 0,
        posterPath = "",
        releaseDate = "",
        title = "Flight Club",
        rating = "",
        overview = "This movie is good.",
        isFavorite = false,
      )
      val movieDetails = MovieDetails(
        id = 1123,
        posterPath = "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "9.5",
        isFavorite = false,
        overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a " +
          "shocking new form of therapy. Their concept catches on, with underground fight clubs forming in every town," +
          " until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
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
          movieId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          isLoading = true,
        ),

        DetailsViewState(
          movieId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          movieDetails = movieDetails,
        ),

        DetailsViewState(
          movieId = popularMovie.id,
          mediaType = MediaType.TV,
          movieDetails = movieDetails,
          similarMovies = similarMovies,
        ),

        DetailsViewState(
          movieId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          movieDetails = movieDetails,
          similarMovies = similarMovies,
          reviews = reviews,
        ),

        DetailsViewState(
          movieId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          error = UIText.StringText("Something went wrong.")
        ),
      )
    }
}
