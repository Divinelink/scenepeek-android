package com.divinelink.core.domain.change

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.extensions.calculateFourteenDayRange
import com.divinelink.core.commons.extensions.isDateToday
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.domain.change.person.PersonChangesActionFactory
import com.divinelink.core.network.media.model.changes.ChangesParameters
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlin.time.Clock

class FetchChangesUseCase(
  private val repository: PersonRepository,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : UseCase<Long, Unit>(dispatcher.io) {

  override suspend fun execute(parameters: Long) {
    val person = repository.fetchPersonDetails(parameters).first().getOrNull()

    val dateRange = person?.insertedAt?.calculateFourteenDayRange(clock)

    dateRange?.let { range ->
      range.forEach { dateRange ->
        if (dateRange.first.isDateToday(clock)) {
          Napier.d("Skipping today's date $dateRange")
          return@forEach
        }

        Napier.d("Fetching changes for person $parameters for $dateRange")
        repository.fetchPersonChanges(
          id = parameters,
          params = ChangesParameters(
            startDate = dateRange.first,
            endDate = dateRange.second,
          ),
        ).collect { result ->

          result
            .getOrNull()
            ?.changes
            ?.filter {
              // Only apply changes with that apply to the current locale.
              it.items.any { item -> item.iso6391 == "en" || item.iso31661 == "" }
            }
            ?.forEach { change ->
              PersonChangesActionFactory(repository)
                .getAction(change.key)
                ?.execute(id = parameters, items = change.items)
            }
        }
        repository.updatePerson(parameters, insertedAt = clock.currentEpochSeconds())
      }
    }
  }
}
