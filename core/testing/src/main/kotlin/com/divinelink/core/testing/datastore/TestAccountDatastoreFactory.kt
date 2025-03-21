package com.divinelink.core.testing.datastore

import androidx.datastore.core.DataStore
import com.divinelink.core.datastore.AccountDetailsProto

class TestAccountDatastoreFactory {

  val accountDatastore: DataStore<AccountDetailsProto> = InMemoryDataStore(
    AccountDetailsProto.getDefaultInstance(),
  )

  companion object {
    fun create(): DataStore<AccountDetailsProto> = TestAccountDatastoreFactory().accountDatastore
  }
}
