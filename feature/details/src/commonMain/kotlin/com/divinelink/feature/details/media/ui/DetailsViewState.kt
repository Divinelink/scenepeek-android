package com.divinelink.feature.details.media.ui

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.LCEState
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.media.DetailsForms
import com.divinelink.core.model.details.provider.WatchProviders
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.jellyseerr.permission.canManageRequests
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.ui.snackbar.SnackbarMessage

@Immutable
data class DetailsViewState(
  val isLoading: Boolean,
  val mediaType: MediaType,
  val mediaId: Int,
  val mediaDetails: MediaDetails?,
  val userDetails: AccountMediaDetails,
  val accountDataState: Map<AccountDataSection, Boolean>,
  val trailer: Video?,
  val error: UIText?,
  val snackbarMessage: SnackbarMessage?,
  val navigateToLogin: Boolean?,
  val menuOptions: List<DetailsMenuOptions>,
  val actionButtons: List<DetailActionItem>,
  val spoilersObfuscated: Boolean,
  val ratingSource: RatingSource,
  val watchProviders: LCEState<WatchProviders>?,
  val selectedTabIndex: Int,
  val jellyseerrMediaInfo: JellyseerrMediaInfo?,
  val permissions: List<ProfilePermission>,
  val tabs: List<Tab>,
  val forms: DetailsForms,
) {
  companion object {
    fun initial(
      isLoading: Boolean,
      mediaId: Int,
      mediaType: MediaType,
      tabs: List<Tab>,
      forms: DetailsForms,
    ) = DetailsViewState(
      isLoading = isLoading,
      mediaType = mediaType,
      mediaId = mediaId,
      mediaDetails = null,
      userDetails = AccountMediaDetails.initial,
      accountDataState = AccountDataSection.entries.associateWith { false },
      trailer = null,
      error = null,
      snackbarMessage = null,
      navigateToLogin = null,
      menuOptions = emptyList(),
      actionButtons = emptyList(),
      spoilersObfuscated = false,
      ratingSource = RatingSource.TMDB,
      watchProviders = LCEState.Loading,
      selectedTabIndex = 0,
      jellyseerrMediaInfo = null,
      permissions = emptyList(),
      tabs = tabs,
      forms = forms,
    )
  }

  val mediaItem = when (mediaDetails) {
    is Movie -> MediaItem.Media.Movie(
      id = mediaDetails.id,
      name = mediaDetails.title,
      backdropPath = mediaDetails.backdropPath,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      voteAverage = mediaDetails.ratingCount.getRating(ratingSource)?.voteAverage ?: 0.0,
      voteCount = mediaDetails.ratingCount.getRating(ratingSource)?.voteCount ?: 0,
      overview = mediaDetails.overview ?: "",
      popularity = mediaDetails.popularity,
      isFavorite = mediaDetails.isFavorite,
    )
    is TV -> MediaItem.Media.TV(
      id = mediaDetails.id,
      name = mediaDetails.title,
      backdropPath = mediaDetails.backdropPath,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      voteAverage = mediaDetails.ratingCount.getRating(ratingSource)?.voteAverage ?: 0.0,
      voteCount = mediaDetails.ratingCount.getRating(ratingSource)?.voteCount ?: 0,
      overview = mediaDetails.overview ?: "",
      popularity = mediaDetails.popularity,
      isFavorite = mediaDetails.isFavorite,
    )
    null -> null
  }

  val canManageRequests
    get() = permissions.canManageRequests || jellyseerrMediaInfo?.requests?.isNotEmpty() == true
}
