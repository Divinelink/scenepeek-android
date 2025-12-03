package com.divinelink.feature.onboarding.manager

import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take

class ProdIntroManager(
  private val onboardingStorage: OnboardingStorage,
  private val currentVersion: Int = getBuildConfigProvider().versionCode,
) : OnboardingManager {

  override val showIntro: Flow<Boolean> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->
    isFirstLaunch || hasUnseenChangelogs(lastSeenVersion, currentVersion)
  }

  override val isInitialOnboarding: Flow<Boolean> = onboardingStorage.isFirstLaunch.take(1)

  override val sections: Flow<List<IntroSection>> = combine(
    onboardingStorage.isFirstLaunch,
    onboardingStorage.lastSeenVersion,
  ) { isFirstLaunch, lastSeenVersion ->
    if (isFirstLaunch) {
      IntroSections.onboardingSections
    } else {
      getUnseenChangelogSections(lastSeenVersion, currentVersion)
    }
  }.take(1)

  override suspend fun onOnboardingComplete() {
    onboardingStorage.setOnboardingCompleted()
  }

  private fun hasUnseenChangelogs(
    lastSeenVersion: Int,
    currentVersion: Int,
  ): Boolean = (lastSeenVersion + 1..currentVersion).any { version ->
    IntroSections.changelogSections.containsKey(version)
  }

  private fun getUnseenChangelogSections(
    lastSeenVersion: Int,
    currentVersion: Int,
  ): List<IntroSection> {
    val unseenSections = mutableListOf<IntroSection>()

    for (version in currentVersion downTo lastSeenVersion + 1) {
      IntroSections.changelogSections[version]?.let { sections ->
        unseenSections.addAll(sections)
      }
    }

    return unseenSections
      .filterIndexed { index, section ->
        if (section is IntroSection.Header) {
          unseenSections.indexOfFirst { it is IntroSection.Header } == index
        } else {
          true
        }
      }
  }
}
