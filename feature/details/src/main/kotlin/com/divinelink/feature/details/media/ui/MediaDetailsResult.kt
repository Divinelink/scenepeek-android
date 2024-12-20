package com.divinelink.feature.details.media.ui

import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.UIText
import com.divinelink.feature.details.R

/**
 * A collection of possible results for an attempt to fetch movie details, similar movies and reviews.
 */
sealed class MediaDetailsResult {
  data class AccountDetailsSuccess(val accountDetails: AccountMediaDetails) : MediaDetailsResult()

  data class DetailsSuccess(
    val mediaDetails: MediaDetails,
    val ratingSource: RatingSource,
  ) : MediaDetailsResult()

  data class ReviewsSuccess(val reviews: List<Review>) : MediaDetailsResult()

  data class SimilarSuccess(val similar: List<MediaItem.Media>) : MediaDetailsResult()

  data class VideosSuccess(val trailer: Video?) : MediaDetailsResult()

  data class CreditsSuccess(val aggregateCredits: AggregateCredits) : MediaDetailsResult()

  data class MenuOptionsSuccess(val menuOptions: List<DetailsMenuOptions>) : MediaDetailsResult()

  data class ActionButtonsSuccess(val actionButtons: List<DetailActionItem>) :
    MediaDetailsResult()

  sealed class Failure(
    open val message: UIText = UIText.ResourceText(R.string.general_error_message),
  ) : MediaDetailsResult() {

    data class FatalError(
      override val message: UIText = UIText.ResourceText(
        R.string.details__fatal_error_fetching_details,
      ),
    ) : Failure(
      message = message,
    )

    data object Unknown : Failure()
  }
}
