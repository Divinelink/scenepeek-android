package com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds

import com.divinelink.core.commons.di.DefaultDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class ObserveBlackBackgroundsUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @DefaultDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Boolean>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<Boolean>> =
    preferenceStorage.isBlackBackgroundsEnabled.map {
      Result.success(it)
    }
}
