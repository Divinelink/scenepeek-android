package com.divinelink.core.datastore.crypto

import app.cash.turbine.test
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DataStoreKeystoreSecretsStorageTest {

  private val dataStore = DataStoreKeystoreSecretsStorage(
    dataStore = TestDatastoreFactory.create(),
  )

  @Test
  fun `test setIvByteArray for secret for saved state key`() = runTest {
    dataStore.getIvByteArray(EncryptionSecretKey.SAVED_STATE_KEY).test {
      dataStore.setIvByteArray(
        secret = EncryptionSecretKey.SAVED_STATE_KEY,
        iv = "text to store".encodeToByteArray(),
      )

      awaitItem() shouldBe byteArrayOf()
      awaitItem() shouldBe "text to store".encodeToByteArray()
    }
  }

  @Test
  fun `test getIvByteArray without saved preference`() = runTest {
    dataStore.getIvByteArray(EncryptionSecretKey.SAVED_STATE_KEY).test {
      awaitItem() shouldBe byteArrayOf()
    }
  }

  @Test
  fun `test getIvByteArray with empty preference`() = runTest {
    dataStore.setIvByteArray(
      secret = EncryptionSecretKey.SAVED_STATE_KEY,
      iv = "".encodeToByteArray(),
    )

    dataStore.getIvByteArray(EncryptionSecretKey.SAVED_STATE_KEY).test {
      awaitItem() shouldBe byteArrayOf()
    }
  }
}
