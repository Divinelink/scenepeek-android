package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.domain.theme.material.you.MaterialYouProvider

class GetAvailableColorSystemsUseCase(
  private val materialYouProvider: MaterialYouProvider,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, List<ColorSystem>>(dispatcher.default) {
  override suspend fun execute(parameters: Unit): List<ColorSystem> = buildList {
    ColorSystem.entries.forEach { preference ->
      when (preference) {
        ColorSystem.Default -> add(preference)
        ColorSystem.Dynamic -> if (materialYouProvider.isAvailable()) {
          add(preference)
        }
        ColorSystem.Custom -> add(preference)
      }
    }
  }
}
