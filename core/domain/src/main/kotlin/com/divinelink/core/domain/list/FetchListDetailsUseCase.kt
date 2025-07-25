package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    repository.fetchListDetails(
      parameters.listId,
      parameters.page,
    )
      .distinctUntilChanged()
      .collect { result ->
        when (result) {
          is Resource.Error<*> -> emit(Result.failure(result.error))
          is Resource.Loading<ListDetails?> -> result.data?.let {
            emit(Result.success(it))
          }
          is Resource.Success<ListDetails?> -> result.data?.let {
            emit(Result.success(it))
          }
        }
      }
  }
}
