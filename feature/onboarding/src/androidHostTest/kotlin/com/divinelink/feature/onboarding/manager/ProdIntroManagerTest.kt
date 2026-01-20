package com.divinelink.feature.onboarding.manager

import app.cash.turbine.test
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.testing.storage.TestOnboardingStorage
import com.divinelink.feature.onboarding.resources.feature_onboarding_changelog
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_convert_app_to_multiplatform
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_display_fullscreen_posters
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_external_ratings
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_fix_encryption
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_fix_jellyfin_auth_with_empty_passwords
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_custom_color_option
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_detailed_csrf_warning
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_detailed_error_messages_for_seerr
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_improve_search
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_show_year_on_search
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_fix_duplicate_ids_crash
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_redesign_home_screen
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_update_favorites
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
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
      awaitItem() shouldBe true
      awaitComplete()
    }
  }

  @Test
  fun `test isInitialOnboarding is false when isFirstLaunch is false`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false),
    )

    manager.isInitialOnboarding.test {
      awaitItem() shouldBe false
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
      awaitItem() shouldBe false

      manager.onOnboardingComplete()

      awaitItem() shouldBe true
    }
  }

  @Test
  fun `test onboardingPages with firstLaunch returns initial pages`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.sections.test {
      awaitItem() shouldBe IntroSections.onboardingSections
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
      awaitItem() shouldBe emptyList()
      awaitComplete()
    }
  }

  @Test
  fun `test shouldShowOnboarding with firstLaunch returns true`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = true),
    )

    manager.showIntro.test {
      awaitItem() shouldBe true
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 15 but current version 16`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 15),
      currentVersion = 16,
    )

    manager.showIntro.test {
      awaitItem() shouldBe false
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 21 but current version 23`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 32),
      currentVersion = 33,
    )

    manager.showIntro.test {
      awaitItem() shouldBe true
    }

    manager.sections.test {
      awaitItem() shouldContainExactly IntroSections.v33
      awaitComplete()
    }
  }

  @Test
  fun `test showIntro for lastSeenVersion 29 but current version 33 shows all unseen`() = runTest {
    manager = ProdIntroManager(
      onboardingStorage = TestOnboardingStorage(isFirstLaunch = false, lastSeenVersion = 29),
      currentVersion = 33,
    )

    manager.showIntro.test {
      awaitItem() shouldBe true
    }

    manager.sections.test {
      val sections = awaitItem()
      sections shouldContainExactly listOf(
        IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
        IntroSection.WhatsNew("v0.24.0"),
        IntroSection.SecondaryHeader.Added,
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v33_redesign_home_screen),
        ),
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v33_update_favorites),
        ),
        IntroSection.SecondaryHeader.Fixed,
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v33_fix_duplicate_ids_crash),
        ),
        IntroSection.WhatsNew("v0.23.0"),
        IntroSection.SecondaryHeader.Added,
        IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v32_improve_search)),
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v32_add_custom_color_option),
        ),
        IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v32_show_year_on_search)),
        IntroSection.SecondaryHeader.Fixed,
        IntroSection.Text(
          UIText.ResourceText(
            R.string.feature_onboarding_v32_add_detailed_error_messages_for_seerr,
          ),
        ),
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v32_add_detailed_csrf_warning),
        ),
        IntroSection.WhatsNew("v0.22.0"),
        IntroSection.SecondaryHeader.Added,
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v30_convert_app_to_multiplatform),
        ),
        IntroSection.Text(
          UIText.ResourceText(R.string.feature_onboarding_v30_display_fullscreen_posters),
        ),
        IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v30_external_ratings)),
        IntroSection.SecondaryHeader.Fixed,
        IntroSection.Text(
          UIText.ResourceText(
            R.string.feature_onboarding_v30_fix_jellyfin_auth_with_empty_passwords,
          ),
        ),
        IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v30_fix_encryption)),
      )
      awaitComplete()
    }
  }
}
