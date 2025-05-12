package com.divinelink.feature.details.media

sealed interface DetailsData {
  data class TVAbout(
    val overview: String,
    val tagline: String,
    val genres: List<String>,
  ) : DetailsData

  data class MovieAbout(
    val overview: String,
    val tagline: String,
    val genres: List<String>,
  ) : DetailsData
}

sealed interface DetailsForm<T : DetailsData> {
  data object Loading : DetailsForm<Nothing>
  data object Error : DetailsForm<Nothing>
  data class Content<T : DetailsData>(val data: T) : DetailsForm<T>
}

// sealed interface DetailsForm {
//
//  sealed interface About : DetailsForm {
//    data object Loading : About
//    data object Error : About
//    data class Content(
//      val overview: String,
//      val tagline: String,
//      val genres: List<String>,
//    ) : About
//  }
//
//  sealed interface TvCast : DetailsForm {
//    data object Loading : TvCast
//    data object Error : TvCast
//    data class Content(val credits: AggregateCredits) : TvCast
//  }
//
//  sealed interface MovieCast : DetailsForm {
//    data object Loading : MovieCast
//    data object Error : MovieCast
//    data class Content(val cast: List<Person>) : MovieCast
//  }
//
//  data object Seasons : DetailsForm
//  data object Recommendations : DetailsForm
//  data object Reviews : DetailsForm
// }
