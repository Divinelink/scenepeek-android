package com.divinelink.feature.onboarding.di

import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.feature.onboarding.manager.ProdIntroManager
import org.koin.dsl.module

val onboardingModule = module {

  single<OnboardingManager> {
    ProdIntroManager(
      onboardingStorage = get(),
      currentVersion = getBuildConfigProvider().versionCode,
    )
  }
}
