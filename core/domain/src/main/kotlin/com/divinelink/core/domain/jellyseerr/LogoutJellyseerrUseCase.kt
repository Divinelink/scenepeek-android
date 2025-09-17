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
    val account = authRepository.selectedJellyseerrCredentials.first()

    if (account == null) {
      authRepository.clearSelectedJellyseerrAccount()
      Result.success(Unit)
    } else {
      val response = repository.logout(account.address)
        .onFailure {
          authRepository.clearSelectedJellyseerrAccount()
        }.onSuccess {
          authRepository.clearSelectedJellyseerrAccount()
        }

      Result.success(response.data)
    }
  }
}
