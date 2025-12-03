package com.divinelink.core.commons.provider

actual fun getConstantsProvider(): ConstantsProvider = NativeConstantsProvider()

class NativeConstantsProvider : ConstantsProvider {
  override val backdropUrl: String = "https://image.tmdb.org/t/p/w780"
  override val imageUrl: String = "https://image.tmdb.org/t/p/w342"
}
