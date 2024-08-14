package com.andreolas.movierama.base.di

import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.media.service.ProdMediaService
import com.divinelink.core.network.session.service.ProdSessionService
import com.divinelink.core.network.session.service.SessionService
import org.koin.dsl.module

// TODO move those to the respective modules
val appRemoteModule = module {
  single<MediaService> { ProdMediaService(get()) }
  single<SessionService> { ProdSessionService(get()) }
}
