package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.ListException
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class AddItemParameters(
  val listId: Int,
  val mediaId: Int,
  val mediaType: MediaType,
)

class AddItemToListUseCase(
  private val repository: ListRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<AddItemParameters, Boolean>(dispatcher.default) {

  override fun execute(parameters: AddItemParameters): Flow<Result<Boolean>> = flow {
    val result = repository.addItemToList(
      parameters.listId,
      parameters.mediaId,
      parameters.mediaType.value,
    )
    result.fold(
      onSuccess = {
        when (it) {
          AddToListResult.Success -> emit(Result.success(true))
          AddToListResult.Failure.ItemAlreadyExists -> emit(
            Result.failure(ListException.ItemAlreadyExists()),
          )
          AddToListResult.Failure.UnexpectedError -> emit(
            Result.failure(ListException.UnexpectedError()),
          )
        }
      },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
