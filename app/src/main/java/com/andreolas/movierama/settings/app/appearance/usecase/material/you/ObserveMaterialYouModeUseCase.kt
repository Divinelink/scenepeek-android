package com.andreolas.movierama.settings.app.appearance.usecase.material.you

import com.andreolas.movierama.base.di.DefaultDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.divinelink.core.commons.domain.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class ObserveMaterialYouModeUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @DefaultDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Boolean>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<Boolean>> {
    return preferenceStorage.isMaterialYouEnabled.map {
      Result.success(it)
    }
  }
}
