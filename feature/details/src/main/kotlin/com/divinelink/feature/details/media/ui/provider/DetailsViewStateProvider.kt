package com.divinelink.feature.details.media.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.feature.details.media.ui.DetailsViewState

@Suppress("MagicNumber")
@ExcludeFromKoverReport
class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {
  override val values: Sequence<DetailsViewState>
    get() {
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
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            numberOfSeasons = 0,
          ),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            status = TvStatus.UNKNOWN,
          ),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          userDetails = AccountMediaDetails(
            id = 0,
            favorite = false,
            rating = 9.0f,
            watchlist = true,
          ),
        ),
        DetailsViewState(
          mediaId = popularMovie.id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          error = UIText.StringText("Something went wrong."),
        ),
      )
    }
}
