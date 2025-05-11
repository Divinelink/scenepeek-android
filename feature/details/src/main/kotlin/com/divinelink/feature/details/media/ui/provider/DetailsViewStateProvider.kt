package com.divinelink.feature.details.media.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.details.media.ui.DetailsViewState

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
