package com.divinelink.core.network.omdb.service

import com.divinelink.core.commons.provider.SecretProvider
import com.divinelink.core.network.client.OMDbClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.divinelink.core.network.omdb.util.buildOMDbUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdOMDbService(
  private val client: OMDbClient,
  private val secrets: SecretProvider,
) : OMDbService {

  override fun fetchExternalRatings(imdbId: String): Flow<OMDbResponseApi> = flow {
    val url = buildOMDbUrl(imdbId = imdbId, apikey = secrets.omdbApiKey)

    val response = client.client.get<OMDbResponseApi>(url)
    emit(response)
  }
}
