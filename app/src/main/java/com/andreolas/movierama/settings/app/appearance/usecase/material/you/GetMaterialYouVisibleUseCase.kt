package com.andreolas.movierama.settings.app.appearance.usecase.material.you

import android.os.Build
import com.andreolas.movierama.base.di.MainDispatcher
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetMaterialYouVisibleUseCase @Inject constructor(
  @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Boolean>(dispatcher) {

  override suspend fun execute(parameters: Unit): Boolean = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
    else -> false
  }
}
