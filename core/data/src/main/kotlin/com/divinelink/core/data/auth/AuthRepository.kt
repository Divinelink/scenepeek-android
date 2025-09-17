package com.divinelink.core.data.auth

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  val isJellyseerrEnabled: Flow<Boolean>
  val jellyseerrCredentials: Flow<Map<String, SavedState.JellyseerrCredentials>>
  val selectedJellyseerrCredentials: Flow<SavedState.JellyseerrCredentials?>
  val selectedJellyseerrProfile: Flow<JellyseerrProfile?>
  val profilePermissions: Flow<List<ProfilePermission>>

  suspend fun updateJellyseerrCredentials(account: SavedState.JellyseerrCredentials)
  suspend fun updateJellyseerrProfile(profile: JellyseerrProfile)
  suspend fun clearSelectedJellyseerrAccount()
}
