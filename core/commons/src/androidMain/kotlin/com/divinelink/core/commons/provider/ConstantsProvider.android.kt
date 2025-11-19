package com.divinelink.core.commons.provider

import com.divinelink.core.commons.BuildConfig

actual fun getConstantsProvider(): ConstantsProvider = AndroidImageConfigProvider()

class AndroidImageConfigProvider : ConstantsProvider {
  override val backdropUrl: String = BuildConfig.TMDB_BACKDROP_PATH_URL
  override val imageUrl: String = BuildConfig.TMDB_IMAGE_URL
}
