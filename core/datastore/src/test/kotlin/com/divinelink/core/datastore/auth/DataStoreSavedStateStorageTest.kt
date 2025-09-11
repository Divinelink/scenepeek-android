package com.divinelink.core.datastore.auth

import app.cash.turbine.test
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.divinelink.core.testing.factories.core.commons.JsonFactory
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.factories.datastore.crypto.TestDataEncryptor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DataStoreSavedStateStorageTest {

  private lateinit var dataStore: DataStoreSavedStateStorage

  @BeforeTest
  fun setup() {
    dataStore = DataStoreSavedStateStorage(
      dataStore = TestDatastoreFactory.create(),
      encryptor = TestDataEncryptor(),
      json = JsonFactory.jsonHelper,
      scope = TestScope(UnconfinedTestDispatcher()),
    )
  }

  @Test
  fun `test initial saved state`() = runTest {
    dataStore.savedState.test {
      awaitItem() shouldBe InitialSavedState
    }
  }

  @Test
  fun `test set jellyseerr auth cookies`() = runTest {
    val testCookie = "test-cookie-value"
    val secondCookie = "test-second-cookie-value"

    dataStore.savedState.test {
      awaitItem() shouldBe InitialSavedState
      dataStore.setJellyseerrAuthCookie(testCookie)

      val nextEmission = awaitItem()

      nextEmission.jellyseerrAuthCookies.values shouldContainExactly listOf(testCookie)

      dataStore.setJellyseerrAuthCookie(secondCookie)

      val secondEmission = awaitItem()
      secondEmission.jellyseerrAuthCookies.values shouldContainExactly listOf(
        testCookie,
        secondCookie,
      )

      dataStore.clearSelectedJellyseerrAccount()

      val thirdEmission = awaitItem()
      thirdEmission.jellyseerrAuthCookies.values shouldContainExactly listOf(testCookie)
    }
  }

  @Test
  fun `test set jellyseerr account without selected account id`() = runTest {
    shouldThrow<IllegalArgumentException> {
      dataStore.setJellyseerrAccount(JellyseerrAccountFactory.zabaob())
    }
  }

  @Test
  fun `test set jellyseerr account with selected account`() = runTest {
    dataStore.setJellyseerrAuthCookie("test-cookie-value")
    dataStore.setJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    dataStore.savedState.test {
      val first = awaitItem()

      first.jellyseerrAccounts.values shouldContainExactly listOf(
        JellyseerrAccountFactory.zabaob(),
      )
    }
  }

  @Test
  fun `test clearSelectedJellyseerrAccount without selected account`() = runTest {
    dataStore.savedState.test {
      awaitItem() shouldBe InitialSavedState
      dataStore.clearSelectedJellyseerrAccount()
    }
  }
}
