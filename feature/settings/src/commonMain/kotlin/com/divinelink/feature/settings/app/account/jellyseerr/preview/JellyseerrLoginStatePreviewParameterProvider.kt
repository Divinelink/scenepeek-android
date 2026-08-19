package com.divinelink.feature.settings.app.account.jellyseerr.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
import com.divinelink.core.model.jellyseerr.SeerrState
import com.divinelink.core.model.jellyseerr.login.SeerrLoginForm

@ExcludeFromKoverReport
class JellyseerrLoginStatePreviewParameterProvider :
  PreviewParameterProvider<SeerrState.Login> {
  override val values: Sequence<SeerrState.Login> = sequenceOf(
    SeerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(),
    ),
    SeerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        method = SeerrAuthMethod.SEERR,
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
      ),
    ),
    SeerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        method = SeerrAuthMethod.JELLYFIN,
      ),
    ),
    SeerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password("password"),
        method = SeerrAuthMethod.EMBY,
      ),
    ),
    SeerrState.Login(
      isLoading = false,
      loginForm = SeerrLoginForm(
        address = Address.from("https://jellyseerr.com"),
        username = Username.from("DavidLynch"),
        password = Password.empty,
        method = SeerrAuthMethod.EMBY,
      ),
    ),
    SeerrState.Login(
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
