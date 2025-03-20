package com.divinelink.core.datastore.account

import androidx.datastore.core.DataStore
import com.divinelink.core.datastore.AccountDetailsProto
import com.divinelink.core.datastore.copy
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

interface AccountStorage {
  suspend fun setAccountDetails(accountDetails: AccountDetails)
  suspend fun clearAccountDetails()
  val accountDetails: Flow<AccountDetails?>
  val accountId: Flow<String?>
}

class AccountPreferenceStorage(private val dataStore: DataStore<AccountDetailsProto>) :
  AccountStorage {

  companion object {
    const val PREFS_NAME = "account.preferences"
  }

  override suspend fun setAccountDetails(accountDetails: AccountDetails) {
    dataStore.updateData { preferences ->
      Timber.d("Storing account details to memory: $accountDetails")
      preferences.copy {
        id = accountDetails.id
        name = accountDetails.name
        username = accountDetails.username
        accountDetails.tmdbAvatarPath?.let { avatar ->
          this.avatar = avatar
        }
      }
    }
  }

  override suspend fun clearAccountDetails() {
    dataStore.updateData { preferences ->
      preferences.toBuilder().clear().build()
    }
  }

  override val accountId: Flow<String?> = dataStore.data.map { preferences ->
    if (preferences.id == 0) {
      return@map null
    }

    return@map preferences.id.toString()
  }

  override val accountDetails: Flow<AccountDetails?> = dataStore.data.map { preferences ->
    Timber.d("Retrieved account details from memory: $preferences")
    if (preferences.id == 0) {
      null
    } else {
      AccountDetails(
        id = preferences.id,
        username = preferences.username,
        name = preferences.name,
        tmdbAvatarPath = if (preferences.avatar.isNullOrEmpty()) null else preferences.avatar,
      )
    }
  }
}
