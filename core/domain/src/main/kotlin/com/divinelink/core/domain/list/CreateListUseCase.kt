package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.network.Resource
import com.divinelink.core.network.list.model.CreateListRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

data class CreateListParameters(
  val name: String,
  val description: String,
  val public: Boolean,
)

class CreateListUseCase(
  private val repository: ListRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<CreateListParameters, Int>(dispatcher.default) {

  override fun execute(parameters: CreateListParameters): Flow<Result<Int>> = flow {
    repository.createList(
      request = CreateListRequest.create(
        name = parameters.name,
        description = parameters.description,
        public = parameters.public,
      ),
    ).fold(
      onSuccess = { createListResult ->
        if (createListResult.success) {
          val accountId = sessionStorage.accountId
          if (accountId == null) {
            emit(Result.failure(SessionException.Unauthenticated()))
          } else {
            repository.fetchUserLists(
              accountId = accountId,
              page = 1,
            )
              .distinctUntilChanged()
              .collect {
                when (it) {
                  is Resource.Success,
                  is Resource.Loading,
                  -> emit(Result.success(createListResult.id))
                  else -> emit(Result.failure(Exception("Failed to fetch user lists")))
                }
              }
          }
        } else {
          emit(Result.failure(Exception("Failed to create list")))
        }
      },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
