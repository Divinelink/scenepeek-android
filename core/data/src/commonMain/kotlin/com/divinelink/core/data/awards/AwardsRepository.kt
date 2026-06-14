package com.divinelink.core.data.awards

import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.model.awards.YearAwards

interface AwardsRepository {

  suspend fun fetchAwardCeremonies(): Result<List<Ceremony>>

  suspend fun fetchCeremonyCategories(id: String): Result<List<CeremonyCategory>>

  suspend fun fetchAwards(
    ceremonyId: String,
    categoryId: String,
  ): Result<List<YearAwards>>
}
