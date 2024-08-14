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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// TODO Transfer to domain module
val settingsUseCaseModule = module {

  singleOf(::SetThemeUseCase)
  singleOf(::GetThemeUseCase)
  singleOf(::GetAvailableThemesUseCase)
  singleOf(::SetMaterialYouUseCase)
  singleOf(::GetMaterialYouUseCase)
  singleOf(::SetBlackBackgroundsUseCase)
  singleOf(::GetBlackBackgroundsUseCase)
  singleOf(::GetMaterialYouVisibleUseCase)

  singleOf(::GetBlackBackgroundsUseCase)
  singleOf(::ObserveBlackBackgroundsUseCase)
  singleOf(::ObserveThemeModeUseCase)
  singleOf(::ObserveMaterialYouModeUseCase)
}
