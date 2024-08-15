package com.divinelink.feature.settings.app.appearance.usecase.material.you

import android.os.Build
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase

open class GetMaterialYouVisibleUseCase(dispatcher: DispatcherProvider) :
  UseCase<Unit, Boolean>(dispatcher.main) {

  override suspend fun execute(parameters: Unit): Boolean = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
    else -> false
  }
}
