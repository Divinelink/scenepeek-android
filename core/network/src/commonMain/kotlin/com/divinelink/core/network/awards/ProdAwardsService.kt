package com.divinelink.core.network.awards

import com.divinelink.core.network.awards.model.awards.AwardsResponse
import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse
import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse
import com.divinelink.core.network.awards.util.buildAwardCeremoniesUrl
import com.divinelink.core.network.awards.util.buildCeremonyCategoriesUrl
import com.divinelink.core.network.awards.util.buildGetAwardsCategoryUrl
import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.client.get

class ProdAwardsService(
  val restClient: RestClient,
) : AwardsService {

  override suspend fun fetchAwardCeremonies(): Result<CeremoniesResponse> = runCatching {
    restClient.client.get<CeremoniesResponse>(
      url = buildAwardCeremoniesUrl(),
    )
  }

  override suspend fun fetchCeremonyCategories(id: String): Result<CeremonyCategoriesResponse> =
    runCatching {
      restClient.client.get<CeremonyCategoriesResponse>(
        url = buildCeremonyCategoriesUrl(id = id),
      )
    }

  override suspend fun fetchAwards(
    ceremonyId: String,
    categoryId: String,
  ): Result<AwardsResponse> = runCatching {
    restClient.client.get<AwardsResponse>(
      url = buildGetAwardsCategoryUrl(
        ceremonyId = ceremonyId,
        categoryId = categoryId,
      ),
    )
  }
}
