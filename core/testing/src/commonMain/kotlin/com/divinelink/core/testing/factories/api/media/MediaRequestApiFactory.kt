package com.divinelink.core.testing.factories.api.media

import com.divinelink.core.network.media.model.MediaRequestApi

object MediaRequestApiFactory {
  fun movie() = MediaRequestApi.Movie(555)

  fun tv() = MediaRequestApi.TV(555)
}
