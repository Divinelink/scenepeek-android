package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.extensions.calculateFourteenDayRange
import com.divinelink.core.commons.extensions.isDateToday
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.network.changes.model.serializer.ChangeValue
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.datetime.Clock
import timber.log.Timber

class FetchChangesUseCase(
  private val repository: PersonRepository,
  private val personDao: PersonDao,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : UseCase<Long, Result<List<ChangeValue>>>(dispatcher.io) {

  override suspend fun execute(parameters: Long): Result<List<ChangeValue>> {
    personDao.fetchPersonById(parameters).collectLatest {
      val dateRange = it?.insertedAt?.calculateFourteenDayRange(clock)

      dateRange?.let { range ->
        range.forEach { dateRange ->
          if (dateRange.first.isDateToday(clock)) {
            Timber.d("Skipping today's date $dateRange")
            return@forEach
          }

          Timber.d("Fetching changes for person $parameters for $dateRange")
          repository.fetchPersonChanges(
            id = parameters,
            params = ChangesParameters(
              startDate = dateRange.first,
              endDate = dateRange.second,
            ),
          ).last()
        }
      }
    }

    return Result.success(emptyList())
  }
}
