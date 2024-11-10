package com.divinelink.scenepeek.base.di

import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.media.service.ProdMediaService
import com.divinelink.core.network.session.service.ProdSessionService
import com.divinelink.core.network.session.service.SessionService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// TODO move those to the respective modules
val appRemoteModule = module {
  singleOf(::ProdMediaService) { bind<MediaService>() }
  singleOf(::ProdSessionService) { bind<SessionService>() }
}
