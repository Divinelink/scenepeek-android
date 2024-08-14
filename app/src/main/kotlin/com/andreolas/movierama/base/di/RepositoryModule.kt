package com.andreolas.movierama.base.di

import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.details.repository.ProdDetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.data.session.repository.ProdSessionRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.network.session.service.SessionService
import org.koin.dsl.module

val appRepositoryModule = module {

  single<MediaRepository> {
    ProdMediaRepository(get(), get())
  }

  single<DetailsRepository> {
    ProdDetailsRepository(get(), get(), get())
  }

  single<SessionRepository> {
    val remote: SessionService = get()

    ProdSessionRepository(remote)
  }
}
