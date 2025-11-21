package com.divinelink.core.datastore

import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.observedTmdbSession
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.model.session.TmdbSession
import kotlinx.coroutines.flow.Flow

class SessionStorage(
  val savedState: SavedStateStorage,
) {
  val sessionId: String?
    get() = savedState.savedState.value.tmdbSession?.sessionId

  val sessionFlow: Flow<TmdbSession?> = savedState.observedTmdbSession

  // V4 Account Object ID
  val accountId: String?
    get() = savedState
      .savedState
      .value
      .tmdbSession
      ?.accessToken
      ?.accountId

  suspend fun setAccessToken(
    sessionId: String,
    accessToken: AccessToken,
  ) {
    savedState.setTMDBSession(
      session = TmdbSession(
        sessionId = sessionId,
        accessToken = accessToken,
      ),
    )
  }
}
