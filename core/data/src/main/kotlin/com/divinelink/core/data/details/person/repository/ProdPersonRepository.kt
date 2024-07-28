package com.divinelink.core.data.details.person.repository

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.data.details.person.mapper.map
import com.divinelink.core.data.details.person.mapper.mapToEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.network.details.person.service.PersonService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import javax.inject.Inject

class ProdPersonRepository @Inject constructor(
  private val service: PersonService,
  private val dao: PersonDao,
  private val clock: Clock,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : PersonRepository {

  override fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>> = channelFlow {
    dao.fetchPersonById(id).collectLatest { personDetails ->
      if (personDetails != null) {
        // Fetch from database
        send(Result.success(personDetails.map()))
        return@collectLatest
      } else {
        // Fetch from network and save to database, then emitted through the channel
        service.fetchPersonDetails(id).collectLatest { response ->
          dao.insertPerson(response.mapToEntity(clock.currentEpochSeconds()))
        }
      }
    }
  }
}
