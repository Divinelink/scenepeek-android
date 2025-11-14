package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged

data class JellyseerrProfileResult(
  val address: String,
  val profile: JellyseerrProfile?,
)

class GetJellyseerrProfileUseCase(
  private val authRepository: AuthRepository,
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Boolean, JellyseerrProfileResult>(dispatcher.default) {

  /**
   * @param parameters: If true, fetch from remote
   */
  override fun execute(parameters: Boolean): Flow<Result<JellyseerrProfileResult>> = channelFlow {
    authRepository
      .selectedJellyseerrCredentials
      .distinctUntilChanged()
      .collect { account ->
        if (account != null) {
          repository.getJellyseerrProfile(
            address = account.address,
            refresh = parameters,
          ).collect { result ->
            when (result) {
              is Resource.Error -> send(Result.failure(result.error))
              is Resource.Loading -> send(
                Result.success(
                  JellyseerrProfileResult(
                    address = account.address,
                    profile = result.data,
                  ),
                ),
              )
              is Resource.Success -> send(
                Result.success(
                  JellyseerrProfileResult(
                    address = account.address,
                    profile = result.data,
                  ),
                ),
              )
            }
          }
        } else {
          send(
            Result.success(
              JellyseerrProfileResult(
                address = "",
                profile = null,
              ),
            ),
          )
        }
      }
  }
}
