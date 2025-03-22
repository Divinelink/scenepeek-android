package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.account.AccountStorage
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAccountStorage(accountDetails: AccountDetails? = null) : AccountStorage {

  private val _accountDetails = MutableStateFlow(accountDetails)
  override val accountDetails: Flow<AccountDetails?> = _accountDetails

  override val accountId: Flow<String?> = _accountDetails.map { it?.id?.toString() }

  override suspend fun setAccountDetails(accountDetails: AccountDetails) {
    _accountDetails.value = accountDetails
  }

  override suspend fun clearAccountDetails() {
    _accountDetails.value = null
  }
}
