package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionRepository
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoginUseCase @Inject constructor(
  private val repository: SessionRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Result<String>>(dispatcher) {

  override suspend fun execute(parameters: Unit): Result<String> {
    val result = repository.createRequestToken()

    return when (result) {
      is Result.Success -> {
        Result.Success(result.data.url)
      }
      is Result.Error -> Result.Error(result.exception)
      is Result.Loading -> Result.Loading
    }
  }
}
