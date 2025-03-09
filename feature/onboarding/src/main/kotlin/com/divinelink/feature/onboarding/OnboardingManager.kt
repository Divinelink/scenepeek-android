package com.divinelink.feature.onboarding

import com.divinelink.core.commons.BuildConfig
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface OnboardingManager {
  val shouldShowOnboarding: Flow<Boolean>
  val onboardingPages: Flow<List<OnboardingPage>>
  suspend fun onOnboardingComplete()
  suspend fun handleOnboardingAction(action: OnboardingAction)
}

class ProdOnboardingManager(
  private val onboardingStorage: OnboardingStorage,
  private val currentVersion: Int = BuildConfig.VERSION_CODE,
) : OnboardingManager {
  override val shouldShowOnboarding: Flow<Boolean> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->

    isFirstLaunch || (lastSeenVersion != currentVersion && hasNewPagesForUpdate(lastSeenVersion))
    true
  }

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

      OnboardingPages.initialPages + newPages
    }
  }

  override suspend fun onOnboardingComplete() {
    onboardingStorage.setOnboardingCompleted()
  }

  override suspend fun handleOnboardingAction(action: OnboardingAction) {
    TODO("Not yet implemented")
  }

  private fun hasNewPagesForUpdate(lastSeenVersion: Int): Boolean = OnboardingPages
    .newFeaturePages
    .any { (version, _) ->
      version > lastSeenVersion
    }
}
