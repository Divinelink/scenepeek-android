package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

open class LogoutJellyseerrUseCase @Inject constructor(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, String>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<String>> = flow {
    val address = storage.jellyseerrAddress.first()
    if (address == null) {
      emit(Result.failure(Exception("No address found.")))
      return@flow
    }

    emit(
      repository.logout(address).last().fold(
        onSuccess = {
          sessionStorage.clearJellyseerrSession()
          repository.clearJellyseerrAccountDetails()
          Result.success(address)
        },
        onFailure = {
          Result.failure(it)
        },
      ),
    )
  }
}
