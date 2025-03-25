package com.divinelink.feature.onboarding.manager

import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.OnboardingPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProdOnboardingManager(
  private val onboardingStorage: OnboardingStorage,
  private val currentVersion: Int = BuildConfig.VERSION_CODE,
) : OnboardingManager {
  override val shouldShowOnboarding: Flow<Boolean> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    isFirstLaunch || (lastSeenVersion != currentVersion && hasNewPagesForUpdate(lastSeenVersion))
  }

  override val isInitialOnboarding: Flow<Boolean> = onboardingStorage.isFirstLaunch

  override val onboardingPages: Flow<List<OnboardingPage>> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    if (isFirstLaunch) {
      OnboardingPages.initialPages
    } else {
      val newPages = OnboardingPages.newFeaturePages
        .filter { (version, _) -> version > lastSeenVersion }
        .flatMap { (_, pages) -> pages }

      newPages
    }
  }

  override suspend fun onOnboardingComplete() {
    onboardingStorage.setOnboardingCompleted()
  }

  private fun hasNewPagesForUpdate(lastSeenVersion: Int): Boolean = OnboardingPages.newFeaturePages
    .any { (version, _) ->
      version > lastSeenVersion
    }
}
