package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.details.person.model.PersonCombinedCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdPersonService @Inject constructor(private val restClient: RestClient) : PersonService {

  override fun fetchPersonDetails(id: Long): Flow<PersonDetailsApi> = flow {
    val url = "${restClient.tmdbUrl}/person/$id"

    emit(restClient.get<PersonDetailsApi>(url = url))
  }

  override fun fetchPersonCombinedCredits(id: Long): Flow<PersonCombinedCreditsApi> = flow {
    val url = "${restClient.tmdbUrl}/person/$id/combined_credits"

    emit(restClient.get<PersonCombinedCreditsApi>(url = url))
  }
}
