package com.divinelink.scenepeek.settings.appearance.usecase.material.you

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.domain.theme.material.you.GetMaterialYouVisibleUseCase

class FakeGetMaterialYouVisibleUseCase(dispatcher: DispatcherProvider) :
  GetMaterialYouVisibleUseCase(dispatcher) {
  private var result: Result<Boolean> = Result.failure(Exception())

  fun mockMaterialYouVisible(response: Boolean) {
    result = Result.success(response)
  }

  override suspend fun execute(parameters: Unit): Boolean = result.data
}
