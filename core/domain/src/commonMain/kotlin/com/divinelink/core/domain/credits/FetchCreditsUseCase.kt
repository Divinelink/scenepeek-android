package com.divinelink.core.domain.credits

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.credits.AggregateCredits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCreditsUseCase(
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Long, AggregateCredits>(dispatcher.io) {

  override fun execute(parameters: Long): Flow<Result<AggregateCredits>> = flow {
    repository.fetchAggregateCredits(parameters).collect {
      emit(Result.success(it.data))
    }
  }
}
