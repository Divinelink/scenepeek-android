package com.divinelink.core.testing.domain.theme.material.you

import com.divinelink.core.domain.theme.material.you.MaterialYouProvider

object MaterialYouProviderFactory {

  val available = object : MaterialYouProvider {
    override fun isAvailable(): Boolean = true
  }

  val unavailable = object : MaterialYouProvider {
    override fun isAvailable(): Boolean = false
  }
}
