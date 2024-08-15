package com.divinelink.feature.settings.di

import com.divinelink.feature.settings.app.appearance.usecase.GetAvailableThemesUseCase
import com.divinelink.feature.settings.app.appearance.usecase.GetThemeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.ObserveThemeModeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.SetThemeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds.ObserveBlackBackgroundsUseCase
import com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.GetMaterialYouUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.GetMaterialYouVisibleUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.ObserveMaterialYouModeUseCase
import com.divinelink.feature.settings.app.appearance.usecase.material.you.SetMaterialYouUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

// TODO Transfer to domain module
val settingsUseCaseModule = module {

  factoryOf(::SetThemeUseCase)
  factoryOf(::GetThemeUseCase)
  factoryOf(::GetAvailableThemesUseCase)
  factoryOf(::SetMaterialYouUseCase)
  factoryOf(::GetMaterialYouUseCase)
  factoryOf(::SetBlackBackgroundsUseCase)
  factoryOf(::GetBlackBackgroundsUseCase)
  factoryOf(::GetMaterialYouVisibleUseCase)

  factoryOf(::GetBlackBackgroundsUseCase)
  factoryOf(::ObserveBlackBackgroundsUseCase)
  factoryOf(::ObserveThemeModeUseCase)
  factoryOf(::ObserveMaterialYouModeUseCase)
}
