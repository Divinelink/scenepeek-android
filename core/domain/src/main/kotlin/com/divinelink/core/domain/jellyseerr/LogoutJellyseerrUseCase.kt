package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.flow.first

class LogoutJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val address = sessionStorage.storage.jellyseerrAddress.first()
    if (address == null) {
      sessionStorage.clearJellyseerrSession()
      repository.clearJellyseerrAccountDetails()
      Result.success(Unit)
    } else {
      val response = repository.logout(address)
        .onFailure {
          sessionStorage.clearJellyseerrSession()
          repository.clearJellyseerrAccountDetails()
        }.onSuccess {
          sessionStorage.clearJellyseerrSession()
          repository.clearJellyseerrAccountDetails()
        }

      Result.success(response.data)
    }
  }
}
