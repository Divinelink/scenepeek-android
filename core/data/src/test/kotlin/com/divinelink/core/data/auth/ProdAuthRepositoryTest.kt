package com.divinelink.core.data.auth

import app.cash.turbine.test
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.storage.TestSavedStateStorage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProdAuthRepositoryTest {

  private val savedStateStorage = TestSavedStateStorage()
  private val repository = ProdAuthRepository(savedStateStorage)

  @Test
  fun `test isJellyseerrEnabled is true when accountIds are not empty`() = runTest {
    repository.isJellyseerrEnabled.test {
      awaitItem() shouldBe false

      savedStateStorage.setJellyseerrAccount(JellyseerrAccountFactory.zabaob())

      awaitItem() shouldBe false

      savedStateStorage.setJellyseerrAuthCookie("test-cookie-value")

      awaitItem() shouldBe true
    }
  }

  @Test
  fun `test jellyseerrAccounts`() = runTest {
    repository.jellyseerrAccounts.test {
      awaitItem() shouldBe emptyMap()

      savedStateStorage.setJellyseerrAccount(JellyseerrAccountFactory.zabaob())

      awaitItem() shouldBe mapOf(
        "account_1" to JellyseerrAccountFactory.zabaob(),
      )

      savedStateStorage.setJellyseerrAccount(JellyseerrAccountFactory.cup10())

      awaitItem() shouldBe mapOf(
        "account_1" to JellyseerrAccountFactory.cup10(),
      )
    }
  }

  @Test
  fun `test updateJellyseerrAccount sets jellyseerrAccount`() = runTest {
    savedStateStorage.savedState.value.jellyseerrAccounts shouldBe emptyMap()

    repository.updateJellyseerrAccount(JellyseerrAccountFactory.cup10())

    savedStateStorage.savedState.value.jellyseerrAccounts shouldBe mapOf(
      "account_1" to JellyseerrAccountFactory.cup10(),
    )
  }

  @Test
  fun `test clearSelectedJellyseerrAccount`() = runTest {
    savedStateStorage.savedState.value.jellyseerrAccounts shouldBe emptyMap()

    repository.updateJellyseerrAccount(JellyseerrAccountFactory.cup10())

    savedStateStorage.savedState.value.jellyseerrAccounts shouldBe mapOf(
      "account_1" to JellyseerrAccountFactory.cup10(),
    )

    repository.clearSelectedJellyseerrAccount()

    savedStateStorage.savedState.value.jellyseerrAccounts shouldBe emptyMap()
  }
}
