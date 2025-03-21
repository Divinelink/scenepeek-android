package com.divinelink.scenepeek.base.storage

import app.cash.turbine.test
import com.divinelink.core.datastore.account.AccountPreferenceStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.testing.datastore.TestAccountDatastoreFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test

class AccountPreferenceStorageTest {

  private lateinit var storage: AccountPreferenceStorage

  private val dataStore = TestAccountDatastoreFactory.create()

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test preferences with accountId 0 returns null account details`() = runTest {
    storage = AccountPreferenceStorage(dataStore)

    assertThat(storage.accountId.first()).isNull()

    storage.setAccountDetails(AccountDetailsFactory.Pinkman().copy(id = 0))

    storage.accountId.test {
      assertThat(awaitItem()).isNull()
    }
    storage.accountDetails.test {
      assertThat(awaitItem()).isNull()
    }
  }

  @Test
  fun `test setAccountDetails correctly sets accountId and details`() = runTest {
    storage = AccountPreferenceStorage(dataStore)

    assertThat(storage.accountId.first()).isNull()

    storage.setAccountDetails(AccountDetailsFactory.Pinkman())

    storage.accountId.test {
      assertThat(awaitItem()).isEqualTo("1")
    }
    storage.accountDetails.test {
      assertThat(awaitItem()).isEqualTo(AccountDetailsFactory.Pinkman())
    }
  }

  @Test
  fun `test account details with null or empty avatar returns null avatar`() = runTest {
    storage = AccountPreferenceStorage(dataStore)

    assertThat(storage.accountId.first()).isNull()

    storage.accountDetails.test {
      assertThat(awaitItem()).isNull()

      storage.setAccountDetails(AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = ""))
      assertThat(awaitItem()).isEqualTo(AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = null))

      storage.setAccountDetails(AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = "123"))
      assertThat(awaitItem()).isEqualTo(
        AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = "123"),
      )

      storage.setAccountDetails(AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = null))
      assertThat(awaitItem()).isEqualTo(AccountDetailsFactory.Pinkman().copy(tmdbAvatarPath = null))
    }
  }

  @Test
  fun `test clearAccountId removes accountId`() = runTest {
    storage = AccountPreferenceStorage(dataStore)

    storage.setAccountDetails(AccountDetailsFactory.Pinkman())

    assertThat(storage.accountId.first()).isEqualTo("1")

    storage.clearAccountDetails()

    assertThat(storage.accountId.first()).isNull()
  }
}
