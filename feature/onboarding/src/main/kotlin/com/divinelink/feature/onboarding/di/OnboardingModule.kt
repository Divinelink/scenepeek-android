package com.divinelink.feature.onboarding.di

import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.feature.onboarding.manager.ProdIntroManager
import org.koin.dsl.module

val onboardingModule = module {

  single<OnboardingManager> {
    ProdIntroManager(
      onboardingStorage = get(),
      currentVersion = BuildConfig.VERSION_CODE,
    )
  }
}
