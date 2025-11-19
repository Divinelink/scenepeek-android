package com.divinelink.core.commons.provider

expect fun getConstantsProvider(): ConstantsProvider

interface ConstantsProvider {
  val imageUrl: String
  val backdropUrl: String
}
