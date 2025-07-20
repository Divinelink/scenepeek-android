package com.divinelink.core.domain.list

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.user.data.UserDataSorting
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged

data class UserListsParameters(
  val page: Int,
  val sortBy: UserDataSorting = UserDataSorting.DESCENDING,
)

class FetchUserListsUseCase(
  private val storage: SessionStorage,
  private val repository: ListRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<UserListsParameters, PaginationData<ListItem>>(dispatcher.default) {

  override fun execute(parameters: UserListsParameters): Flow<Result<PaginationData<ListItem>>> =
    channelFlow {
      storage.accountStorage.accountId.collect { accountId ->
        if (accountId == null) {
          send(Result.failure(SessionException.Unauthenticated()))
        } else {
          val v4AccountId = storage.accountId
          val accessToken = storage.encryptedStorage.accessToken

          if (v4AccountId == null || accessToken == null) {
            send(Result.failure(SessionException.Unauthenticated()))
            return@collect
          }

          repository.fetchUserLists(
            accountId = v4AccountId,
            page = parameters.page,
          )
            .distinctUntilChanged()
            .collect { result ->
              when (result) {
                is Resource.Error -> send(Result.failure(result.error))
                is Resource.Loading<PaginationData<ListItem>?> -> result.data?.let { listItems ->
                  send(
                    Result.success(
                      listItems.copy(
                        list = listItems.list.sortedWith(listsComparator),
                      ),
                    ),
                  )
                }

                is Resource.Success<PaginationData<ListItem>?> -> result.data?.let { listItems ->
                  send(
                    Result.success(
                      listItems.copy(
                        list = listItems.list.sortedWith(listsComparator),
                      ),
                    ),
                  )
                }
              }
            }
        }
      }
    }
}
