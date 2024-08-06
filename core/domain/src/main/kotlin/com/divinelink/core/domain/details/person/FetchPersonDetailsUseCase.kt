package com.divinelink.core.domain.details.person

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.data.person.repository.PersonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FetchPersonDetailsUseCase @Inject constructor(
  private val repository: PersonRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Long, PersonDetailsResult>(dispatcher) {

  override fun execute(parameters: Long): Flow<Result<PersonDetailsResult>> = channelFlow {
    launch(dispatcher) {
      repository.fetchPersonDetails(parameters)
        .catch {
          Timber.e(it)
          send(Result.failure(PersonDetailsResult.DetailsFailure))
        }
        .collect { result ->
          result.fold(
            onFailure = {
              Timber.e(it)
              send(Result.failure(PersonDetailsResult.DetailsFailure))
            },
            onSuccess = { send(Result.success(PersonDetailsResult.DetailsSuccess(result.data))) },
          )
        }
    }
  }
}
