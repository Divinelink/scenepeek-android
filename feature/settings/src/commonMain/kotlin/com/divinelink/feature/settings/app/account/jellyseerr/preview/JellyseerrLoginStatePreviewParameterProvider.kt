package com.divinelink.feature.settings.app.account.jellyseerr.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState

@ExcludeFromKoverReport
class JellyseerrLoginStatePreviewParameterProvider :
  PreviewParameterProvider<JellyseerrState.Login> {
  override val values: Sequence<JellyseerrState.Login> = sequenceOf(
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData.empty(),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = SeerrAuthMethod.SEERR,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = SeerrAuthMethod.JELLYFIN,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = SeerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password.empty(),
        authMethod = SeerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = true,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = SeerrAuthMethod.EMBY,
      ),
    ),
  )
}
