package com.divinelink.scenepeek.base.di

import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.details.repository.ProdDetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.data.session.repository.ProdSessionRepository
import com.divinelink.core.data.session.repository.SessionRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appRepositoryModule = module {

  singleOf(::ProdMediaRepository) { bind<MediaRepository>() }
  singleOf(::ProdDetailsRepository) { bind<DetailsRepository>() }
  singleOf(::ProdSessionRepository) { bind<SessionRepository>() }
}
