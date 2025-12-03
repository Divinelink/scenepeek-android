package com.divinelink.core.domain.theme.material.you

interface MaterialYouProvider {
  fun isAvailable(): Boolean
}

class ProdMaterialYouProvider : MaterialYouProvider {
  override fun isAvailable(): Boolean = isMaterialYouAvailable()
}

expect fun isMaterialYouAvailable(): Boolean
