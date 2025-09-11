package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import kotlinx.coroutines.flow.first

class LogoutJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val address = authRepository.selectedJellyseerrAccount.first()?.address
    if (address == null) {
      authRepository.clearSelectedJellyseerrAccount()
      repository.clearJellyseerrAccountDetails()
      Result.success(Unit)
    } else {
      val response = repository.logout(address)
        .onFailure {
          authRepository.clearSelectedJellyseerrAccount()
          repository.clearJellyseerrAccountDetails()
        }.onSuccess {
          authRepository.clearSelectedJellyseerrAccount()
          repository.clearJellyseerrAccountDetails()
        }

      Result.success(response.data)
    }
  }
}
