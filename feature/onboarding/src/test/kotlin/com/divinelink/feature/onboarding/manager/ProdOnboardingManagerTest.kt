package com.divinelink.feature.onboarding.manager

import app.cash.turbine.test
import com.divinelink.core.model.onboarding.OnboardingPage
import com.divinelink.core.testing.storage.TestOnboardingStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProdOnboardingManagerTest {

  private lateinit var manager: ProdOnboardingManager

  @Test
  fun `test isInitialOnboarding is true when isFirstLaunch is true`() = runTest {
    manager = ProdOnboardingManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.isInitialOnboarding.test {
      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test isInitialOnboarding is false when isFirstLaunch is false`() = runTest {
    manager = ProdOnboardingManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false),
    )

    manager.isInitialOnboarding.test {
      assertThat(awaitItem()).isFalse()
    }
  }

  @Test
  fun `test onOnboardingComplete sets flag to true`() = runTest {
    val storage = TestOnboardingStorage(isFirstLaunch = true)

    manager = ProdOnboardingManager(
      onboardingStorage = storage,
    )

    storage.onboardingCompleted.test {
      assertThat(awaitItem()).isFalse()

      manager.onOnboardingComplete()

      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test onboardingPages with firstLaunch returns initial pages`() = runTest {
    manager = ProdOnboardingManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.onboardingPages.test {
      assertThat(awaitItem()).containsExactlyElementsIn(OnboardingPages.initialPages)
    }
  }

  @Test
  fun `test onboardingPages with new feature pages`() = runTest {
    val storage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 1)

    manager = ProdOnboardingManager(
      onboardingStorage = storage,
      currentVersion = 2,
    )

    manager.onboardingPages.test {
      assertThat(awaitItem()).isEqualTo(emptyList<OnboardingPage>())
    }
  }

  @Test
  fun `test shouldShowOnboarding with firstLaunch returns true`() = runTest {
    manager = ProdOnboardingManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.shouldShowOnboarding.test {
      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test shouldShowOnboarding for lastSeenVersion 15 but current version 16`() = runTest {
    manager = ProdOnboardingManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 15),
      currentVersion = 16,
    )

    manager.shouldShowOnboarding.test {
      assertThat(awaitItem()).isFalse()
    }
  }
}
