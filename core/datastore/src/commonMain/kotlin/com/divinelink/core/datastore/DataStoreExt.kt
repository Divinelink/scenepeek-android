package com.divinelink.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

suspend fun <T> DataStore<Preferences>.removeKey(key: Preferences.Key<T>) = edit {
  if (it.contains(key)) {
    it.remove(key)
  }
}
