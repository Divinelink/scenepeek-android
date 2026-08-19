package com.divinelink.feature.settings.app.account.jellyseerr.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
import com.divinelink.core.model.jellyseerr.login.SeerrLoginForm

@ExcludeFromKoverReport
class JellyseerrLoginStatePreviewParameterProvider :
  PreviewParameterProvider<JellyseerrState.Login> {
  override val values: Sequence<JellyseerrState.Login> = sequenceOf(
    JellyseerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        method = SeerrAuthMethod.SEERR,
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        method = SeerrAuthMethod.JELLYFIN,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        method = SeerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password.empty,
        method = SeerrAuthMethod.EMBY,
      ),
    ),
    JellyseerrState.Login(
      isLoading = true,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        method = SeerrAuthMethod.EMBY,
      ),
    ),
  )
}
