package com.divinelink.feature.onboarding.manager

import app.cash.turbine.test
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.testing.storage.TestOnboardingStorage
import com.divinelink.feature.onboarding.resources.feature_onboarding_changelog
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_feature_profile
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_feature_tmdb_lists
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_fix_encryption
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_add_loading_indicator
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_retry_failed_api_calls
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_update_changelog
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import com.divinelink.feature.onboarding.resources.Res as R

class ProdIntroManagerTest {

  private lateinit var manager: ProdIntroManager

  @Test
  fun `test isInitialOnboarding is true when isFirstLaunch is true`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.isInitialOnboarding.test {
      assertThat(awaitItem()).isTrue()
      awaitComplete()
    }
  }

  @Test
  fun `test isInitialOnboarding is false when isFirstLaunch is false`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false),
    )

    manager.isInitialOnboarding.test {
      assertThat(awaitItem()).isFalse()
      awaitComplete()
    }
  }

  @Test
  fun `test onOnboardingComplete sets flag to true`() = runTest {
    val storage = TestOnboardingStorage(isFirstLaunch = true)

    manager = ProdIntroManager(
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
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.sections.test {
      assertThat(awaitItem()).containsExactlyElementsIn(IntroSections.onboardingSections)
      awaitComplete()
    }
  }

  @Test
  fun `test onboardingPages with new feature pages`() = runTest {
    val storage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 1)

    manager = ProdIntroManager(
      onboardingStorage = storage,
      currentVersion = 2,
    )

    manager.sections.test {
      assertThat(awaitItem()).isEqualTo(emptyList<IntroSection>())
      awaitComplete()
    }
  }

  @Test
  fun `test shouldShowOnboarding with firstLaunch returns true`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.showIntro.test {
      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 15 but current version 16`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 15),
      currentVersion = 16,
    )

    manager.showIntro.test {
      assertThat(awaitItem()).isFalse()
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 21 but current version 23`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 21),
      currentVersion = 23,
    )

    manager.showIntro.test {
      assertThat(awaitItem()).isTrue()
    }

    manager.sections.test {
      assertThat(awaitItem()).containsExactlyElementsIn(
        IntroSections.v23,
      )
      awaitComplete()
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 21 but current version 24 shows all unseen`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 21),
      currentVersion = 24,
    )

    manager.showIntro.test {
      assertThat(awaitItem()).isTrue()
    }

    manager.sections.test {
      val sections = awaitItem()
      assertThat(sections).containsExactlyElementsIn(
        listOf(
          IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
          IntroSection.WhatsNew("v0.16.0"),
          IntroSection.SecondaryHeader.Fixed,
          IntroSection.Text(
            UIText.ResourceText(R.string.feature_onboarding_v24_retry_failed_api_calls),
          ),
          IntroSection.Text(
            UIText.ResourceText(R.string.feature_onboarding_v24_add_loading_indicator),
          ),
          IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v24_update_changelog)),
          IntroSection.WhatsNew("v0.15.0"),
          IntroSection.SecondaryHeader.Added,
          IntroSection.Text(
            UIText.ResourceText(R.string.feature_onboarding_v22_feature_tmdb_lists),
          ),
          IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_feature_profile)),
          IntroSection.SecondaryHeader.Fixed,
          IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_fix_encryption)),
        ),
      )
      awaitComplete()
    }
  }
}
