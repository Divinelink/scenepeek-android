package com.divinelink.core.data.awards

import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.model.awards.YearAwards
import com.divinelink.core.network.awards.AwardsService
import com.divinelink.core.network.awards.mapper.map

class ProdAwardsRepository(
  private val service: AwardsService,
) : AwardsRepository {

  override suspend fun fetchAwardCeremonies(): Result<List<Ceremony>> = service
    .fetchAwardCeremonies()
    .map { response ->
      response.map()
    }

  override suspend fun fetchAwards(
    ceremonyId: String,
    categoryId: String,
  ): Result<List<YearAwards>> = service.fetchAwards(
    ceremonyId = ceremonyId,
    categoryId = categoryId,
  ).map { response ->
    response.map()
  }

  override suspend fun fetchCeremonyCategories(id: String): Result<List<CeremonyCategory>> = service
    .fetchCeremonyCategories(id = id)
    .map { response ->
      response.map()
    }
}
