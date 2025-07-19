package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.network.list.model.CreateListRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CreateListParameters(
  val name: String,
  val description: String,
  val public: Boolean,
)

class CreateListUseCase(
  private val repository: ListRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<CreateListParameters, Int>(dispatcher.default) {

  override fun execute(parameters: CreateListParameters): Flow<Result<Int>> = flow {
    val result = repository.createList(
      request = CreateListRequest(
        name = parameters.name,
        description = parameters.description,
        public = parameters.public,
      ),
    )
    result.fold(
      onSuccess = {
        if (it.success) {
          emit(Result.success(it.id))
        } else {
          emit(Result.failure(Exception("Failed to create list")))
        }
      },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
