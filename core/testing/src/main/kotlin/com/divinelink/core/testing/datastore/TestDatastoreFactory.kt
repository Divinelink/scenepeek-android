package com.divinelink.core.testing.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.divinelink.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.TestScope
import org.junit.Rule

class TestDatastoreFactory {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val testCoroutineScope = TestScope(testDispatcher)

  private val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  val datastore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
    scope = testCoroutineScope,
    produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) },
  )

  companion object {
    private const val TEST_DATASTORE_NAME: String = "test_datastore"

    fun create(): DataStore<Preferences> = TestDatastoreFactory().datastore
  }
}
