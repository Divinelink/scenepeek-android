package com.divinelink.core.data.auth

import app.cash.turbine.test
import com.divinelink.core.datastore.auth.InitialSavedState
import com.divinelink.core.datastore.auth.observedTmdbSession
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.fixtures.model.session.TmdbSessionFactory
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.storage.TestSavedStateStorage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProdAuthRepositoryTest {

  private lateinit var savedStateStorage: TestSavedStateStorage
  private lateinit var repository: ProdAuthRepository

  @Test
  fun `test isJellyseerrEnabled is true when accountIds are not empty`() = runTest {
    savedStateStorage = TestSavedStateStorage()
    repository = ProdAuthRepository(savedStateStorage)

    repository.isJellyseerrEnabled.test {
      awaitItem() shouldBe false

      savedStateStorage.setJellyseerrCredentials(JellyseerrAccountFactory.zabaob())

      expectNoEvents()

      savedStateStorage.setJellyseerrAuthCookie("test-cookie-value")

      awaitItem() shouldBe true
    }
  }

  @Test
  fun `test jellyseerrAccounts`() = runTest {
    savedStateStorage = TestSavedStateStorage()
    repository = ProdAuthRepository(savedStateStorage)

    repository.jellyseerrCredentials.test {
      awaitItem() shouldBe emptyMap()

      savedStateStorage.setJellyseerrCredentials(JellyseerrAccountFactory.zabaob())

      awaitItem() shouldBe mapOf(
        "account_1" to JellyseerrAccountFactory.zabaob(),
      )

      savedStateStorage.setJellyseerrCredentials(JellyseerrAccountFactory.cup10())

      awaitItem() shouldBe mapOf(
        "account_1" to JellyseerrAccountFactory.cup10(),
      )
    }
  }

  @Test
  fun `test updateJellyseerrCredentials sets jellyseerrCredentials`() = runTest {
    savedStateStorage = TestSavedStateStorage()
    repository = ProdAuthRepository(savedStateStorage)

    savedStateStorage.savedState.value.jellyseerrCredentials shouldBe emptyMap()

    repository.updateJellyseerrCredentials(JellyseerrAccountFactory.cup10())

    savedStateStorage.savedState.value.jellyseerrCredentials shouldBe mapOf(
      "account_1" to JellyseerrAccountFactory.cup10(),
    )
  }

  @Test
  fun `test updateJellyseerrProfile sets jellyseerrProfile`() = runTest {
    savedStateStorage = TestSavedStateStorage()
    repository = ProdAuthRepository(savedStateStorage)

    savedStateStorage.savedState.value.jellyseerrCredentials shouldBe emptyMap()

    repository.updateJellyseerrProfile(JellyseerrProfileFactory.jellyseerr())

    savedStateStorage.savedState.value.jellyseerrProfiles shouldBe mapOf(
      "account_1" to JellyseerrProfileFactory.jellyseerr(),
    )
  }

  @Test
  fun `test clearSelectedJellyseerrAccount`() = runTest {
    savedStateStorage = TestSavedStateStorage(
      jellyseerrCredentials = mapOf(
        "account_1" to JellyseerrAccountFactory.cup10(),
        "account_2" to JellyseerrAccountFactory.zabaob(),
      ),
      jellyseerrProfiles = mapOf(
        "account_1" to JellyseerrProfileFactory.jellyseerr(),
        "account_2" to JellyseerrProfileFactory.jellyfin(),
      ),
      jellyseerrAuthCookies = mapOf(
        "account_1" to "test-cookie-value_1",
        "account_2" to "test-cookie-value_2",
      ),
      selectedJellyseerrAccountId = "account_1",
    )
    repository = ProdAuthRepository(savedStateStorage)

    savedStateStorage.savedState.value.jellyseerrCredentials shouldBe mapOf(
      "account_1" to JellyseerrAccountFactory.cup10(),
      "account_2" to JellyseerrAccountFactory.zabaob(),
    )

    savedStateStorage.savedState.value.jellyseerrProfiles shouldBe mapOf(
      "account_1" to JellyseerrProfileFactory.jellyseerr(),
      "account_2" to JellyseerrProfileFactory.jellyfin(),
    )

    savedStateStorage.savedState.value.jellyseerrAuthCookies shouldBe mapOf(
      "account_1" to "test-cookie-value_1",
      "account_2" to "test-cookie-value_2",
    )

    repository.clearSelectedJellyseerrAccount()

    savedStateStorage.savedState.value.jellyseerrCredentials shouldBe mapOf(
      "account_2" to JellyseerrAccountFactory.zabaob(),
    )

    savedStateStorage.savedState.value.jellyseerrProfiles shouldBe mapOf(
      "account_2" to JellyseerrProfileFactory.jellyfin(),
    )

    savedStateStorage.savedState.value.jellyseerrAuthCookies shouldBe mapOf(
      "account_2" to "test-cookie-value_2",
    )
  }

  @Test
  fun `test set tmdb account`() = runTest {
    savedStateStorage = TestSavedStateStorage(
      tmdbAccount = null,
    )
    repository = ProdAuthRepository(savedStateStorage)


    repository.tmdbAccount.test {
      awaitItem() shouldBe null

      repository.setTMDBAccount(AccountDetailsFactory.Pinkman())

      awaitItem() shouldBe AccountDetailsFactory.Pinkman()
    }
  }

  @Test
  fun `test set tmdb session`() = runTest {
    savedStateStorage = TestSavedStateStorage(
      tmdbAccount = null,
    )
    repository = ProdAuthRepository(savedStateStorage)


    savedStateStorage.observedTmdbSession.test {
      awaitItem() shouldBe null

      repository.setTMDBSession(TmdbSessionFactory.full())

      awaitItem() shouldBe TmdbSessionFactory.full()
    }
  }

  @Test
  fun `test clear tmdb session`() = runTest {
    savedStateStorage = TestSavedStateStorage(
      tmdbAccount = AccountDetailsFactory.Pinkman(),
      tmdbSession = TmdbSessionFactory.full(),
    )
    repository = ProdAuthRepository(savedStateStorage)


    savedStateStorage.savedState.test {
      awaitItem() shouldBe InitialSavedState.copy(
        tmdbSession = TmdbSessionFactory.full(),
        tmdbAccount = AccountDetailsFactory.Pinkman(),
      )

      repository.clearTMDBSession()

      awaitItem() shouldBe InitialSavedState
    }
  }
}
