package com.divinelink.core.commons.provider

actual fun getConstantsProvider(): ConstantsProvider = NativeConstantsProvider()

class NativeConstantsProvider : ConstantsProvider {
  override val backdropUrl: String = "https://image.tmdb.org/t/p/w780" // TODO KMP
  override val imageUrl: String = "https://api.themoviedb.org/3"
}
