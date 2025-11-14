package com.divinelink.core.domain.theme.material.you

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase

open class GetMaterialYouVisibleUseCase(
  private val materialYouProvider: MaterialYouProvider,
  dispatcher: DispatcherProvider,
) :
  UseCase<Unit, Boolean>(dispatcher.default) {

  override suspend fun execute(parameters: Unit): Boolean = when {
    materialYouProvider.isAvailable() -> true
    else -> false
  }
}
