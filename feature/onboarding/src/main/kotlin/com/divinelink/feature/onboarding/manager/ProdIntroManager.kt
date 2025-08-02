package com.divinelink.feature.onboarding.manager

import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ProdIntroManager(
  private val onboardingStorage: OnboardingStorage,
  private val currentVersion: Int = BuildConfig.VERSION_CODE,
) : OnboardingManager {
  override val showIntro: Flow<Boolean> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    isFirstLaunch || (lastSeenVersion != currentVersion && hasChangelogsAvailable(currentVersion))
  }

  override val isInitialOnboarding: Flow<Boolean> = onboardingStorage.isFirstLaunch

  override val sections: Flow<List<IntroSection>> = onboardingStorage
    .isFirstLaunch
    .map { isFirstLaunch ->
      if (isFirstLaunch) {
        IntroSections.onboardingSections
      } else {
        IntroSections.changelogSections[currentVersion] ?: emptyList()
      }
    }

  override suspend fun onOnboardingComplete() {
    onboardingStorage.setOnboardingCompleted()
  }

  private fun hasChangelogsAvailable(currentVersion: Int): Boolean = IntroSections
    .changelogSections
    .containsKey(currentVersion)
}
