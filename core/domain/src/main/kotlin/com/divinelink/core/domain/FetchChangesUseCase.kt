package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.commons.extensions.calculateFourteenDayRange
import com.divinelink.core.commons.extensions.isDateToday
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.domain.change.PersonChangesActionFactory
import com.divinelink.core.model.change.Change
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import timber.log.Timber

class FetchChangesUseCase(
  private val repository: PersonRepository,
  private val personDao: PersonDao,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : UseCase<Long, Result<Unit>>(dispatcher.io) {

  override suspend fun execute(parameters: Long): Result<Unit> {
    val person = personDao.fetchPersonById(parameters).first()
    val changes: MutableList<Change> = mutableListOf()

    val dateRange = person?.insertedAt?.calculateFourteenDayRange(clock)

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
        ).collect { res ->
          changes.addAll(res.data.changes)
        }
      }
    }

    changes
      .filter {
        // Only apply changes with that apply to the current locale.
        it.items.any { item -> item.iso6391 == "en" || item.iso31661 == "" }
      }
      .forEach { change ->
        PersonChangesActionFactory(personDao)
          .getAction(change.key)
          ?.execute(id = parameters, items = change.items)
      }

    return Result.success(Unit)
  }
}
