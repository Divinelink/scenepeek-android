package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetJellyseerrDetailsUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  private val encryptedStorage: EncryptedStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, JellyseerrState>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<JellyseerrState>> =
    storage.jellyseerrAddress.flatMapLatest { address ->
      flow {
        if (address == null) {
//          emit(
//            Result.success(
//              JellyseerrState.Initial(
//                preferredOption = JellyseerrLoginMethod.JELLYSEERR,
//                address = "",
//              ),
//            ),
//          )
          return@flow
        } else {
//          emit(
//            Result.success(
//            ),
//          )
        }
      }
    }
}
