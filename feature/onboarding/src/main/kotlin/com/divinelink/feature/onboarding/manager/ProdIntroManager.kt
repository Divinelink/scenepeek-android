package com.divinelink.feature.onboarding.manager

import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProdIntroManager(
  private val onboardingStorage: OnboardingStorage,
  private val currentVersion: Int = BuildConfig.VERSION_CODE,
) : OnboardingManager {
  override val showIntro: Flow<Boolean> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    isFirstLaunch || (lastSeenVersion != currentVersion && hasNewPagesForUpdate(lastSeenVersion))
  }

  override val isInitialOnboarding: Flow<Boolean> = onboardingStorage.isFirstLaunch

  override val sections: Flow<List<IntroSection>> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    if (isFirstLaunch) {
      IntroSections.onboardingSections
    } else {
      val newPages = IntroSections.changelogSections
        .filter { (version, _) -> version > lastSeenVersion }
        .flatMap { (_, pages) -> pages }

      newPages
    }
  }

  override suspend fun onOnboardingComplete() {
    onboardingStorage.setOnboardingCompleted()
  }

  private fun hasNewPagesForUpdate(lastSeenVersion: Int): Boolean = IntroSections.changelogSections
    .any { (version, _) ->
      version > lastSeenVersion
    }
}
