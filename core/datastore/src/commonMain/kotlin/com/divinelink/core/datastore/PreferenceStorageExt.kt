package com.divinelink.core.datastore

import kotlinx.coroutines.flow.map

val PreferenceStorage.metadataLanguage
  get() = appSettings.map { it.metadataLanguage }
