package com.divinelink.core.network.awards

import com.divinelink.core.network.awards.model.awards.AwardsResponse
import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse
import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse

interface AwardsService {

  suspend fun fetchAwardCeremonies(): Result<CeremoniesResponse>

  suspend fun fetchCeremonyCategories(id: String): Result<CeremonyCategoriesResponse>

  suspend fun fetchAwards(
    ceremonyId: String,
    categoryId: String,
  ): Result<AwardsResponse>
}
