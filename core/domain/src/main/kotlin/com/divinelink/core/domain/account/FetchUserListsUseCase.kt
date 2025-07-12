package com.divinelink.core.domain.account

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.user.data.UserDataSorting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class UserListsParameters(
  val page: Int,
  val sortBy: UserDataSorting = UserDataSorting.DESCENDING,
)

class FetchUserListsUseCase(
  private val storage: SessionStorage,
  private val repository: AccountRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<UserListsParameters, PaginationData<ListItem>>(dispatcher.default) {

  override fun execute(parameters: UserListsParameters): Flow<Result<PaginationData<ListItem>>> =
    flow {
      storage.accountStorage.accountId.collect { accountId ->
        if (accountId == null) {
          emit(Result.failure(SessionException.Unauthenticated()))
        } else {
          val v4AccountId = storage.accountId
          val accessToken = storage.encryptedStorage.accessToken

          if (v4AccountId == null || accessToken == null) {
            emit(Result.failure(SessionException.Unauthenticated()))
            return@collect
          }

          repository.fetchUserLists(v4AccountId).collect { result ->
            result.fold(
              onSuccess = {
                emit(Result.success(it))
              },
              onFailure = {
                emit(Result.failure(it))
              },
            )
          }
        }
      }
    }
}
