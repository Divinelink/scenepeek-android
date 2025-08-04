package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.onboarding.OnboardingStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestOnboardingStorage(
  isFirstLaunch: Boolean = false,
  lastSeenVersion: Int = 0,
  onBoardingCompleted: Boolean = false,
) : OnboardingStorage {

  private val _isFirstLaunch = MutableStateFlow(isFirstLaunch)
  override val isFirstLaunch: Flow<Boolean> = _isFirstLaunch

  private val _lastSeenVersion = MutableStateFlow(lastSeenVersion)
  override val lastSeenVersion: Flow<Int> = _lastSeenVersion

  private val _onboardingCompleted = MutableStateFlow(onBoardingCompleted)
  val onboardingCompleted: Flow<Boolean> = _onboardingCompleted

  override suspend fun setOnboardingCompleted() {
    _onboardingCompleted.value = true
  }
}
