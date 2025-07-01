package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataParameters
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.model.user.data.UserDataSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class FetchUserDataUseCase(
  private val sessionStorage: SessionStorage,
  private val accountRepository: AccountRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<UserDataParameters, UserDataResponse>(dispatcher.default) {

  override fun execute(parameters: UserDataParameters): Flow<Result<UserDataResponse>> = flow {
    val accountId = sessionStorage.accountId
    val sessionId = sessionStorage.sessionId

    if (accountId == null || sessionId == null) {
      emit(Result.failure(SessionException.Unauthenticated()))
      return@flow
    }

    when (parameters.section) {
      UserDataSection.Watchlist -> fetchWatchlist(parameters, accountId, sessionId)
      UserDataSection.Ratings -> fetchRatings(parameters, accountId, sessionId)
    }
  }

  private suspend fun FlowCollector<Result<UserDataResponse>>.fetchRatings(
    parameters: UserDataParameters,
    accountId: String,
    sessionId: String,
  ) {
    if (parameters.mediaType == MediaType.TV) {
      accountRepository.fetchRatedTvShows(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          handleSuccessResponse(
            type = MediaType.TV,
            parameters = parameters,
            response = it,
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    } else {
      accountRepository.fetchRatedMovies(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          handleSuccessResponse(
            type = MediaType.MOVIE,
            parameters = parameters,
            response = it,
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    }
  }

  private suspend fun FlowCollector<Result<UserDataResponse>>.fetchWatchlist(
    parameters: UserDataParameters,
    accountId: String,
    sessionId: String,
  ) {
    if (parameters.mediaType == MediaType.TV) {
      accountRepository.fetchTvShowsWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          handleSuccessResponse(
            type = MediaType.TV,
            parameters = parameters,
            response = it,
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    } else {
      accountRepository.fetchMoviesWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          handleSuccessResponse(
            type = MediaType.MOVIE,
            parameters = parameters,
            response = it,
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    }
  }

  private suspend fun FlowCollector<Result<UserDataResponse>>.handleSuccessResponse(
    type: MediaType,
    parameters: UserDataParameters,
    response: PaginationData<MediaItem.Media>,
  ) {
    val canFetchMore = parameters.page < response.totalPages

    emit(
      Result.success(
        UserDataResponse(
          data = response.list,
          totalResults = response.totalResults,
          type = type,
          canFetchMore = canFetchMore,
        ),
      ),
    )
  }
}
