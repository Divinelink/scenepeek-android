package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.exception.SessionException
import com.andreolas.movierama.details.domain.model.account.AccountMediaDetails
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.session.SessionStorage
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class AccountMediaDetailsParams(
  val id: Int,
  val mediaType: MediaType
)

open class FetchAccountMediaDetailsUseCase @Inject constructor(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<AccountMediaDetailsParams, AccountMediaDetails>(dispatcher) {
  override fun execute(
    parameters: AccountMediaDetailsParams
  ): Flow<Result<AccountMediaDetails>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.NoSession()))
      return@flow
    } else {

      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> AccountMediaDetailsRequestApi.Movie(
          movieId = parameters.id,
          sessionId = sessionId
        )
        MediaType.TV -> AccountMediaDetailsRequestApi.TV(
          seriesId = parameters.id,
          sessionId = sessionId
        )
        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      val response = repository.fetchAccountMediaDetails(
        request = request
      ).last()

      emit(Result.success(response.data))
    }
  }
}
