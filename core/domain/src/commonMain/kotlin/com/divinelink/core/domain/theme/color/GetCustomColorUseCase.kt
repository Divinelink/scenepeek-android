package com.divinelink.core.domain.theme.color

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.first

class GetCustomColorUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Long>(dispatcher.default) {

  override suspend fun execute(parameters: Unit): Long = preferenceStorage
    .customColor
    .first()
}
