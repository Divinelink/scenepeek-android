package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
import com.divinelink.core.model.jellyseerr.login.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.login.SeerrCredentials.Plex
import com.divinelink.core.model.jellyseerr.login.SeerrCredentials.Seerr
import com.divinelink.core.model.jellyseerr.login.SeerrCredentials.StandardAuth.Emby
import com.divinelink.core.model.jellyseerr.login.SeerrCredentials.StandardAuth.Jellyfin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrLoginData, Unit>(dispatcher.default) {

  override fun execute(parameters: JellyseerrLoginData): Flow<Result<Unit>> = flow {
    val result = repository.signIn(parameters)

    val account = when (parameters.credentials) {
      is Seerr -> (parameters.credentials as Seerr).username.value
      is Emby -> (parameters.credentials as Emby).username.value
      is Jellyfin -> (parameters.credentials as Jellyfin).username.value
      is Plex -> ""
    }

    val authMethod = when (parameters.credentials) {
      is Seerr -> SeerrAuthMethod.SEERR
      is Emby -> SeerrAuthMethod.EMBY
      is Jellyfin -> SeerrAuthMethod.JELLYFIN
      is Plex -> SeerrAuthMethod.PLEX
    }

    val password = when (parameters.credentials) {
      is Seerr -> (parameters.credentials as Seerr).password.value
      is Emby -> (parameters.credentials as Emby).password.value
      is Jellyfin -> (parameters.credentials as Jellyfin).password.value
      is Plex -> (parameters.credentials as Plex).authToken.value
    }

    result.fold(
      onSuccess = {
        authRepository.updateJellyseerrCredentials(
          SavedState.JellyseerrCredentials(
            account = account,
            address = parameters.address.normalized,
            authMethod = authMethod,
            password = password,
          ),
        )

        repository.getJellyseerrProfile(
          refresh = true,
          address = parameters.address.value,
        ).collect {
          emit(Result.success(Unit))
        }
      },
      onFailure = {
        emit(Result.failure(it))
      },
    )
  }
}
