package com.divinelink.feature.details.media.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.media.DetailsDataFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory.toTvWzd
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.provider.WatchProvidersFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.model.LCEState
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.feature.details.media.ui.DetailsViewState

@Suppress("MagicNumber", "ThrowingExceptionsWithoutMessageOrCause")
@ExcludeFromKoverReport
class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {
  override val values: Sequence<DetailsViewState>
    get() {
      return sequenceOf(
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = emptyList(),
          forms = emptyMap(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.full(),
        ).copy(
          userDetails = AccountMediaDetails(
            id = 8679,
            favorite = false,
            rating = 9.0f,
            watchlist = false,
          ),
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          forms = DetailsFormFactory.Tv.full(),
          tabs = TvTab.entries,
        ).copy(
          mediaDetails = MediaDetailsFactory.TheOffice(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.full(),
        ).copy(
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            numberOfSeasons = 0,
          ),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = emptyMap(),
        ).copy(
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            information = MediaDetailsFactory.TheOffice().information.copy(
              status = TvStatus.UNKNOWN,
            ),
          ),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.full(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.full(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
          userDetails = AccountMediaDetails(
            id = 0,
            favorite = false,
            rating = 9.0f,
            watchlist = true,
          ),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = emptyMap(),
        ).copy(
          error = UIText.StringText("Something went wrong."),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.full(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Movie.pending(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.full(),
        ).copy(
          mediaDetails = MediaDetailsFactory.TheOffice(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.full().toTvWzd {
            withSeasons(DetailsDataFactory.Tv.seasonsWithStatus())
          },
        ).copy(
          selectedTabIndex = 1,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.error(),
        ).copy(
          selectedTabIndex = 1,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          jellyseerrMediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.error(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
          selectedTabIndex = MovieTab.Recommendations.order,
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.error(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
          selectedTabIndex = MovieTab.Recommendations.order,
        ),
        DetailsViewState.initial(
          isLoading = false,
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.error(),
        ).copy(
          mediaDetails = MediaDetailsFactory.FightClub(),
          selectedTabIndex = MovieTab.Recommendations.order,
          watchProviders = LCEState.Content(data = WatchProvidersFactory.all),
        ),
      )
    }
}
