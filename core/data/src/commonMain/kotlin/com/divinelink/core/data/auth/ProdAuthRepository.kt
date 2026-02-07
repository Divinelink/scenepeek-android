package com.divinelink.core.data.auth

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.isJellyseerrEnabled
import com.divinelink.core.datastore.auth.profilePermissions
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.session.TmdbSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProdAuthRepository(
  private val savedStateStorage: SavedStateStorage,
  private val mediaDao: MediaDao,
) : AuthRepository {

  override val isJellyseerrEnabled: Flow<Boolean> = savedStateStorage
    .savedState
    .map { it.isJellyseerrEnabled }
    .distinctUntilChanged()

  override val profilePermissions: Flow<List<ProfilePermission>> = savedStateStorage
    .savedState
    .map { it.profilePermissions }
    .distinctUntilChanged()

  override val jellyseerrCredentials: Flow<Map<String, SavedState.JellyseerrCredentials>> =
    savedStateStorage
      .savedState
      .map { it.jellyseerrCredentials }
      .distinctUntilChanged()

  override val selectedJellyseerrCredentials: Flow<SavedState.JellyseerrCredentials?> =
    savedStateStorage
      .savedState
      .map { it.jellyseerrCredentials[it.selectedJellyseerrId] }
      .distinctUntilChanged()

  override val selectedJellyseerrProfile: Flow<JellyseerrProfile?> = savedStateStorage
    .savedState
    .map { it.jellyseerrProfiles[it.selectedJellyseerrId] }
    .distinctUntilChanged()

  override val tmdbAccount: Flow<AccountDetails?> = savedStateStorage
    .savedState
    .map { it.tmdbAccount }
    .distinctUntilChanged()

  override suspend fun updateJellyseerrCredentials(account: SavedState.JellyseerrCredentials) {
    savedStateStorage.setJellyseerrCredentials(account)
  }

  override suspend fun updateJellyseerrProfile(profile: JellyseerrProfile) {
    savedStateStorage.setJellyseerrProfile(profile)
  }

  override suspend fun clearSelectedJellyseerrAccount() {
    savedStateStorage.clearSelectedJellyseerrAccount()
  }

  override suspend fun setTMDBAccount(accountDetails: AccountDetails) {
    savedStateStorage.setTMDBAccount(accountDetails)
  }

  override suspend fun setTMDBSession(session: TmdbSession) {
    savedStateStorage.setTMDBSession(session)
  }

  override suspend fun clearTMDBSession() {
    mediaDao.clearAllEpisodeRatings()
    savedStateStorage.clearTMDBSession()
  }
}
