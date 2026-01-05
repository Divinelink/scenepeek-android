package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.designsystem.theme.model.ColorPreference
import com.divinelink.core.domain.theme.material.you.MaterialYouProvider

class GetAvailableColorPreferencesUseCase(
  private val materialYouProvider: MaterialYouProvider,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, List<ColorPreference>>(dispatcher.default) {
  override suspend fun execute(parameters: Unit): List<ColorPreference> = buildList {
    ColorPreference.entries.forEach { preference ->
      when (preference) {
        ColorPreference.Default -> add(preference)
        ColorPreference.Dynamic -> if (materialYouProvider.isAvailable()) {
          add(preference)
        }
        ColorPreference.Custom -> add(preference)
      }
    }
  }
}
