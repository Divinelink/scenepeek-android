package com.divinelink.core.fixtures.details.media

import com.divinelink.core.fixtures.details.credits.SeriesCastFactory
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.media.DetailsData

object DetailsDataFactory {

  object Empty {
    fun about() = DetailsData.About(
      overview = null,
      tagline = null,
      genres = null,
      creators = null,
    )

    fun cast(isTv: Boolean) = DetailsData.Cast(
      isTv = isTv,
      items = emptyList(),
    )

    fun recommendations() = DetailsData.Recommendations(
      items = emptyList(),
    )

    fun reviews() = DetailsData.Reviews(
      items = emptyList(),
    )

    fun seasons() = DetailsData.Seasons(
      items = emptyList(),
    )
  }

  object Movie {
    fun about() = DetailsData.About(
      overview = MediaDetailsFactory.FightClub().overview,
      tagline = MediaDetailsFactory.FightClub().tagline,
      genres = MediaDetailsFactory.FightClub().genres,
      creators = MediaDetailsFactory.FightClub().creators,
    )

    fun cast() = DetailsData.Cast(
      isTv = false,
      items = MediaDetailsFactory.FightClub().cast,
    )

    fun recommendations() = DetailsData.Recommendations(
      items = MediaItemFactory.MoviesList(),
    )

    fun reviews() = DetailsData.Reviews(
      items = ReviewFactory.ReviewList(),
    )
  }

  object Tv {
    fun about() = DetailsData.About(
      overview = MediaDetailsFactory.TheOffice().overview,
      tagline = MediaDetailsFactory.TheOffice().tagline,
      genres = MediaDetailsFactory.TheOffice().genres,
      creators = MediaDetailsFactory.TheOffice().creators,
    )

    fun cast() = DetailsData.Cast(
      isTv = true,
      items = SeriesCastFactory.cast(),
    )

    fun recommendations() = DetailsData.Recommendations(
      items = MediaItemFactory.TVList(),
    )

    fun reviews() = DetailsData.Reviews(
      items = ReviewFactory.ReviewList(),
    )

    fun seasons() = DetailsData.Seasons(
      items = MediaDetailsFactory.TheOffice().seasons,
    )
  }
}
