package com.divinelink.feature.details.media.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
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
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            numberOfSeasons = 0,
          ),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            status = TvStatus.UNKNOWN,
          ),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
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
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          jellyseerrMediaStatus = JellyseerrMediaStatus.AVAILABLE,
        ),

        DetailsViewState(
          mediaId = MediaDetailsFactory.TheOffice().id,
          mediaType = MediaType.TV,
          tabs = TvTab.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
          jellyseerrMediaStatus = JellyseerrMediaStatus.PARTIALLY_AVAILABLE,
        ),
        DetailsViewState(
          mediaId = MediaDetailsFactory.FightClub().id,
          mediaType = MediaType.MOVIE,
          tabs = MovieTab.entries,
          error = UIText.StringText("Something went wrong."),
        ),
      )
    }
}
