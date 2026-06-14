package com.divinelink.core.network.awards.mapper

import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse

fun CeremonyCategoriesResponse.map() = categories.map {
  CeremonyCategory(
    id = it.id,
    title = it.title,
  )
}
