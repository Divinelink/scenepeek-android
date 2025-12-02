package com.divinelink.core.model.details.media

import com.divinelink.core.model.Res
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.fatal_error_fetching_details
import com.divinelink.core.model.general_error_message
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.media.MediaItem

/**
 * A collection of possible results for an attempt to fetch movie details,
 * similar movies and reviews.
 */
sealed class MediaDetailsResult {
  data class AccountDetailsSuccess(val accountDetails: AccountMediaDetails) : MediaDetailsResult()

  data class DetailsSuccess(
    val mediaDetails: MediaDetails,
    val ratingSource: RatingSource,
  ) : MediaDetailsResult()

  data class RatingSuccess(val rating: RatingCount) : MediaDetailsResult()

  data class ReviewsSuccess(
    val formOrder: Int,
    val reviews: List<Review>,
  ) : MediaDetailsResult()

  data class RecommendedSuccess(
    val formOrder: Int,
    val similar: List<MediaItem.Media>,
  ) : MediaDetailsResult()

  data class VideosSuccess(val trailer: Video?) : MediaDetailsResult()

  data class CreditsSuccess(val aggregateCredits: AggregateCredits) : MediaDetailsResult()

  sealed interface JellyseerrDetails {
    data object NotRequested : MediaDetailsResult()
    data class Requested(val info: JellyseerrMediaInfo) : MediaDetailsResult()
  }

  data class MenuOptionsSuccess(val menuOptions: List<DetailsMenuOptions>) : MediaDetailsResult()

  data class ActionButtonsSuccess(val actionButtons: List<DetailActionItem>) : MediaDetailsResult()

  sealed class Failure(
    open val message: UIText = UIText.ResourceText(Res.string.general_error_message),
  ) : MediaDetailsResult() {

    data class FatalError(
      override val message: UIText = UIText.ResourceText(
        Res.string.fatal_error_fetching_details,
      ),
    ) : Failure(
      message = message,
    )

    data object Unknown : Failure()
  }
}
