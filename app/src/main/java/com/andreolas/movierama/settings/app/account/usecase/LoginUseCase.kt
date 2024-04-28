package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.RequestToken
import com.andreolas.movierama.session.SessionRepository
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoginUseCase @Inject constructor(
  private val repository: SessionRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Result<RequestToken>>(dispatcher) {

  override suspend fun execute(parameters: Unit): Result<RequestToken> {
    val result = repository.createRequestToken()

    return result
//    return result.data?.url
//    return when (result) {
//     .isSuccess -> {
//        Result.success(result.data.url)
//      }
//      is Result.failure -> Result.failure(result.exception)
//      is Result.Loading -> Result.Loading
  }
}
