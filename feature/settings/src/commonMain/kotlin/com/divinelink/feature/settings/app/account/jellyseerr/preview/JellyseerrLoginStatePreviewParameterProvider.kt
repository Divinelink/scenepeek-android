package com.divinelink.feature.settings.app.account.jellyseerr.preview

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

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
        authMethod = JellyseerrAuthMethod.JELLYSEERR,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = JellyseerrAuthMethod.JELLYFIN,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = JellyseerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password.empty(),
        authMethod = JellyseerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = true,
      loginData = JellyseerrLoginData(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        authMethod = JellyseerrAuthMethod.EMBY,
      ),
    ),
  )
}
