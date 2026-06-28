package com.divinelink.core.testing.factories.api.awards

import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse
import com.divinelink.core.network.awards.model.category.CeremonyCategoryResponse

object CeremonyCategoriesResponseFactory {

  fun single() = CeremonyCategoryResponse(
    id = "best-picture",
    title = "Best Picture",
  )

  fun all() = CeremonyCategoriesResponse(
    categories = listOf(
      single(),
      CeremonyCategoryResponse(
        id = "best-director",
        title = "Best Director",
      ),
    ),
  )
}
