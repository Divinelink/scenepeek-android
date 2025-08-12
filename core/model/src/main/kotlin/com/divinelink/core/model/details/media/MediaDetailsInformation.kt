package com.divinelink.core.model.details.media

import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface MediaDetailsInformation {

  data class Movie(
    val originalTitle: String,
    val status: String,
    val runtime: String?,
    val originalLanguage: Language?,
    val budget: String,
    val revenue: String,
    val companies: List<String>,
    val countries: List<Country>,
  ) : MediaDetailsInformation

  data object TV : MediaDetailsInformation
}
