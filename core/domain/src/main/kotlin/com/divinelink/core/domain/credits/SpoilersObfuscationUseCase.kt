package com.divinelink.core.domain.credits

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class SpoilersObfuscationUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcherProvider: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcherProvider.io) {
  override fun execute(parameters: Unit): Flow<Result<Boolean>> = preferenceStorage
    .spoilersObfuscation
    .mapLatest { isObfuscated ->
      Result.success(isObfuscated)
    }

  suspend fun setSpoilersObfuscation(isObfuscated: Boolean) {
    preferenceStorage.setSpoilersObfuscation(isObfuscated)
  }
}
