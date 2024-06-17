package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.WatchlistSorting
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class WatchlistParameters(
  val page: Int,
  val sortBy: WatchlistSorting = WatchlistSorting.DESCENDING,
  val mediaType: MediaType,
)

data class WatchlistResponse(
  val data: List<MediaItem.Media>,
  val totalResults: Int,
  val type: MediaType,
  val canFetchMore: Boolean
)

class FetchWatchlistUseCase @Inject constructor(
  @IoDispatcher val dispatcher: CoroutineDispatcher,
  private val sessionStorage: SessionStorage,
  private val accountRepository: AccountRepository
) : FlowUseCase<WatchlistParameters, WatchlistResponse>(dispatcher) {

  override fun execute(parameters: WatchlistParameters): Flow<Result<WatchlistResponse>> = flow {
    val accountId = sessionStorage.accountId.first()

    if (accountId == null) {
      emit(Result.failure(SessionException.InvalidAccountId()))
      return@flow
    }

    if (parameters.mediaType == MediaType.TV) {
      accountRepository.fetchTvShowsWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
      ).last().fold(
        onSuccess = {
          val canFetchMore = parameters.page < it.totalPages

          emit(
            Result.success(
              WatchlistResponse(
                data = it.list,
                totalResults = it.totalResults,
                type = MediaType.TV,
                canFetchMore = canFetchMore
              )
            )
          )
        },
        onFailure = {
          emit(Result.failure(it))
        }
      )
    } else {
      accountRepository.fetchMoviesWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
      ).last().fold(
        onSuccess = {
          val canFetchMore = parameters.page < it.totalPages

          emit(
            Result.success(
              WatchlistResponse(
                data = it.list,
                totalResults = it.totalResults,
                type = MediaType.MOVIE,
                canFetchMore = canFetchMore
              )
            )
          )
        },
        onFailure = {
          emit(Result.failure(it))
        }
      )
    }
  }
}
