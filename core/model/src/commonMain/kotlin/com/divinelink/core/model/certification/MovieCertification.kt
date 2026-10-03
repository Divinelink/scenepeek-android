package com.divinelink.core.model.certification

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.movie_g
import com.divinelink.core.model.resources.movie_nc17
import com.divinelink.core.model.resources.movie_pg
import com.divinelink.core.model.resources.movie_pg13
import com.divinelink.core.model.resources.movie_r
import com.divinelink.core.model.resources.nr_content_rating

object MovieCertification {

  val US: List<Certification> = listOf(
    Certification(
      certification = "NR",
      meaning = UIText.ResourceText(Res.string.nr_content_rating),
      order = 0,
    ),
    Certification(
      certification = "G",
      meaning = UIText.ResourceText(Res.string.movie_g),
      order = 1,
    ),
    Certification(
      certification = "PG",
      meaning = UIText.ResourceText(Res.string.movie_pg),
      order = 2,
    ),
    Certification(
      certification = "PG-13",
      meaning = UIText.ResourceText(Res.string.movie_pg13),
      order = 3,
    ),
    Certification(
      certification = "R",
      meaning = UIText.ResourceText(Res.string.movie_r),
      order = 4,
    ),
    Certification(
      certification = "NC-17",
      meaning = UIText.ResourceText(Res.string.movie_nc17),
      order = 5,
    ),
  )
}
