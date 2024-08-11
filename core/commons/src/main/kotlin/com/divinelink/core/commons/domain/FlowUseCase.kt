package com.divinelink.core.commons.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.failure] to the result) is the subclasses's responsibility.
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
  operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
    .catch { e ->
      Timber.d(e)
      emit(Result.failure(Exception(e)))
    }
    .flowOn(coroutineDispatcher)

  protected abstract fun execute(parameters: P): Flow<Result<R>>
}
