package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.changes.model.api.ChangesResponseApi
import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdPersonService(private val restClient: TMDbClient) : PersonService {

  override fun fetchPersonDetails(id: Long): Flow<PersonDetailsApi> = flow {
    val url = "${restClient.tmdbUrl}/person/$id" +
      "?language=${restClient.metadataLanguage()}"

    emit(restClient.get<PersonDetailsApi>(url = url))
  }

  override fun fetchPersonCombinedCredits(id: Long): Flow<PersonCreditsApi> = flow {
    val url = "${restClient.tmdbUrl}/person/$id/combined_credits" +
      "?language=${restClient.metadataLanguage()}"

    emit(restClient.get<PersonCreditsApi>(url = url))
  }

  override fun fetchPersonChanges(
    id: Long,
    body: ChangesParameters,
  ): Flow<ChangesResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/person/$id/changes" +
      "?page=${body.page}" +
      "&start_date=${body.startDate}" +
      "&end_date=${body.endDate}" +
      "language=${restClient.metadataLanguage()}"

    emit(restClient.get<ChangesResponseApi>(url = url))
  }
}
