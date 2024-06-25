package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class LoginJellyseerrUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  private val encryptedStorage: EncryptedStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : UseCase<JellyseerrState, Unit>(dispatcher) {

  override suspend fun execute(parameters: JellyseerrState) {
//    storage.clearJellyseerrAddress()
    storage.setJellyseerrAddress(parameters.address)
  }
}
