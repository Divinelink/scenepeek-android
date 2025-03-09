package com.divinelink.feature.onboarding.di

import com.divinelink.core.commons.BuildConfig
import com.divinelink.feature.onboarding.OnboardingManager
import com.divinelink.feature.onboarding.ProdOnboardingManager
import org.koin.dsl.module

val onboardingModule = module {

  single<OnboardingManager> {
    ProdOnboardingManager(
      onboardingStorage = get(),
      currentVersion = BuildConfig.VERSION_CODE,
    )
  }
}
