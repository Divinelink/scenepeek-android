package com.andreolas.movierama.session.login.domain.token.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.andreolas.movierama.session.repository.SessionRepository
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class CreateRequestTokenUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  private val repository: SessionRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Result<String>>(dispatcher) {

  override suspend fun execute(parameters: Unit): Result<String> {
    val token = storage.token.first()

    if (token != null) {
      if (token.isNotBlank()) {
        Timber.d("Token already exists in storage")
        return Result.success(token)
      }
    }

    Timber.d("Creating new token")
    val result = repository.createRequestToken()
      .onSuccess { result ->
        Timber.d("Token created successfully")
        storage.setToken(token = result.token)

        Result.success(result.token)
      }

    return result.map { it.token }
  }
}
//
//    storage.token.first()?.let { session ->
//      if (session.status == RequestTokenStatus.PENDING && session.token.isNotBlank()) {
//        Timber.d("Token already exists in storage")
//        return Result.success(RequestToken(session.token))
//      }
//    }
//
//    Timber.d("Creating new token")
//    val result = repository.createRequestToken().onSuccess { result ->
//      Timber.d("Token created successfully")
//      storage.setToken(
//        TMDBToken(
//          token = result.token,
//          status = RequestTokenStatus.PENDING
//        )
//      )
//    }
