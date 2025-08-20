package com.divinelink.feature.details.media.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.media.DetailsDataFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory.toTvWzd
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.feature.details.media.ui.DetailsViewState

@Suppress("MagicNumber")
@ExcludeFromKoverReport
class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {
  override val values: Sequence<DetailsViewState>
    get() {
      return sequenceOf(
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          isLoading = true,
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          userDetails = AccountMediaDetails(
            id = 8679,
            favorite = false,
            rating = 9.0f,
            watchlist = false,
          ),
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.full(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          forms = DetailsFormFactory.Tv.full(),
          tabs = TvTab.entries,
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.full(),
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            numberOfSeasons = 0,
          ),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            information = MediaDetailsFactory.TheOffice().information.copy(
              status = TvStatus.UNKNOWN,
            ),
          ),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.full(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.full(),
          userDetails = AccountMediaDetails(
            id = 0,
            favorite = false,
            rating = 9.0f,
            watchlist = true,
          ),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          error = UIText.StringText("Something went wrong."),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.full(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Movie.pending(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          forms = DetailsFormFactory.Tv.full(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          selectedTabIndex = 1,
          forms = DetailsFormFactory.Tv.full().toTvWzd {
            withSeasons(DetailsDataFactory.Tv.seasonsWithStatus())
          },
          mediaDetails = MediaDetailsFactory.TheOffice(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          selectedTabIndex = 1,
          forms = DetailsFormFactory.Tv.error(),
          mediaDetails = MediaDetailsFactory.TheOffice(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          selectedTabIndex = MovieTab.Recommendations.order,
          forms = DetailsFormFactory.Movie.error(),
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
      )
    }
}
