package com.divinelink.feature.details.media.ui

import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class DetailsViewState(
  val isLoading: Boolean = false,
  val mediaType: MediaType,
  val mediaId: Int,
  val mediaDetails: MediaDetails? = null,
  val userDetails: AccountMediaDetails? = null,
  val tvCredits: AggregateCredits? = null,
  val reviews: List<Review>? = null,
  val similarMovies: List<MediaItem.Media>? = null,
  val trailer: Video? = null,
  val error: UIText? = null,
  val snackbarMessage: SnackbarMessage? = null,
  val showRateDialog: Boolean = false,
  val navigateToLogin: Boolean? = null,
  val menuOptions: List<DetailsMenuOptions> = emptyList(),
  val actionButtons: List<DetailActionItem> = emptyList(),
  val spoilersObfuscated: Boolean = false,
  val ratingSource: RatingSource = RatingSource.TMDB,
) {
  val mediaItem = when (mediaDetails) {
    is Movie -> MediaItem.Media.Movie(
      id = mediaDetails.id,
      name = mediaDetails.title,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      voteAverage = mediaDetails.ratingCount.getRating(ratingSource)?.voteAverage ?: 0.0,
      voteCount = mediaDetails.ratingCount.getRating(ratingSource)?.voteCount ?: 0,
      overview = mediaDetails.overview ?: "",
      isFavorite = mediaDetails.isFavorite,
    )
    is TV -> MediaItem.Media.TV(
      id = mediaDetails.id,
      name = mediaDetails.title,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      voteAverage = mediaDetails.ratingCount.getRating(ratingSource)?.voteAverage ?: 0.0,
      voteCount = mediaDetails.ratingCount.getRating(ratingSource)?.voteCount ?: 0,
      overview = mediaDetails.overview ?: "",
      isFavorite = mediaDetails.isFavorite,
    )
    null -> null
  }
}
