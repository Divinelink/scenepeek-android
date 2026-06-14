package com.divinelink.core.network.awards.mapper

import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse

fun CeremoniesResponse.map() = ceremonies.map {
  Ceremony(
    id = it.id,
    title = it.title,
    url = it.url,
  )
}
