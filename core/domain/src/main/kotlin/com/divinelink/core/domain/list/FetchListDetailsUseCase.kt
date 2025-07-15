package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.list.ListDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class FetchListParameters(
  val listId: Int,
  val page: Int,
)

class FetchListDetailsUseCase(
  private val repository: ListRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<FetchListParameters, ListDetails>(dispatcher.default) {

  override fun execute(parameters: FetchListParameters): Flow<Result<ListDetails>> = flow {
    val result = repository.fetchListDetails(
      parameters.listId,
      parameters.page,
    )
    result.fold(
      onSuccess = { emit(Result.success(it)) },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
