package com.divinelink.core.domain.credits

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.credits.AggregateCredits
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCreditsUseCase @Inject constructor(
  private val repository: DetailsRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Long, AggregateCredits>(dispatcher) {

  override fun execute(parameters: Long): Flow<Result<AggregateCredits>> = flow {
    repository.fetchAggregateCredits(parameters).collect {
      emit(Result.success(it.data))
    }
  }
}
