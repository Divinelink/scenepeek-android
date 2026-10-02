package com.divinelink.core.model.certification

import com.divinelink.core.model.locale.Country

fun Country.movieCertification(): List<Certification> = MovieCertification
  .map[this] ?: MovieCertification.UNITED_STATES

fun Country.showCertification(): List<Certification> = ShowCertification
  .map[this] ?: ShowCertification.UNITED_STATES
