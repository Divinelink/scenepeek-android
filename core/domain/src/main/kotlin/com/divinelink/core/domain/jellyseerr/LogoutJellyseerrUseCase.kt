package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onCompletion

open class LogoutJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, String>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<String>> = flow {
    val address = sessionStorage.storage.jellyseerrAddress.first()
    if (address == null) {
      emit(Result.failure(Exception("No address found.")))
      return@flow
    }

    emit(
      repository
        .logout(address)
        .onCompletion {
          sessionStorage.clearJellyseerrSession()
          repository.clearJellyseerrAccountDetails()
        }
        .last()
        .fold(
          onSuccess = { Result.success(address) },
          onFailure = { Result.failure(it) },
        ),
    )
  }
}
