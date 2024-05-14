package com.andreolas.movierama.details.ui

import com.andreolas.movierama.R
import com.andreolas.movierama.ui.UIText
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem

/**
 * A collection of possible results for an attempt to fetch movie details, similar movies and reviews.
 */
sealed class MovieDetailsResult {
  data class DetailsSuccess(val mediaDetails: MediaDetails) : MovieDetailsResult()

  data class ReviewsSuccess(val reviews: List<Review>) : MovieDetailsResult()

  data class SimilarSuccess(val similar: List<MediaItem.Media>) : MovieDetailsResult()

  data class VideosSuccess(val trailer: Video?) : MovieDetailsResult()

  sealed class Failure(
    open val message: UIText = UIText.ResourceText(R.string.general_error_message),
  ) : MovieDetailsResult() {

    data class FatalError(
      override val message: UIText = UIText.ResourceText(
        R.string.details__fatal_error_fetching_details
      ),
    ) : Failure(
      message = message,
    )

    data object Unknown : Failure()
  }
}
