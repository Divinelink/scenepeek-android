package com.divinelink.core.data.di

import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.account.ProdAccountRepository
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.auth.ProdAuthRepository
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.details.repository.ProdDetailsRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.data.list.ProdListRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.data.preferences.ProdPreferencesRepository
import com.divinelink.core.data.session.repository.ProdSessionRepository
import com.divinelink.core.data.session.repository.RequestTokenManager
import com.divinelink.core.data.session.repository.SessionRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {

  single { RequestTokenManager }
  singleOf(::ProdAccountRepository) { bind<AccountRepository>() }
  singleOf(::ProdMediaRepository) { bind<MediaRepository>() }
  singleOf(::ProdListRepository) { bind<ListRepository>() }
  singleOf(::ProdDetailsRepository) { bind<DetailsRepository>() }
  singleOf(::ProdSessionRepository) { bind<SessionRepository>() }
  singleOf(::ProdJellyseerrRepository) { bind<JellyseerrRepository>() }
  singleOf(::ProdPersonRepository) { bind<PersonRepository>() }
  singleOf(::ProdPreferencesRepository) { bind<PreferencesRepository>() }
  singleOf(::ProdAuthRepository) { bind<AuthRepository>() }
  singleOf(::FilterRepository)
}
