package com.divinelink.core.model.certification

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.nr_content_rating
import com.divinelink.core.model.resources.tv_14
import com.divinelink.core.model.resources.tv_g
import com.divinelink.core.model.resources.tv_ma
import com.divinelink.core.model.resources.tv_pg
import com.divinelink.core.model.resources.tv_y
import com.divinelink.core.model.resources.tv_y7

object ShowCertification {

  val US: List<Certification> = listOf(
    Certification(
      certification = "NR",
      order = 0,
      meaning = UIText.ResourceText(Res.string.nr_content_rating),
    ),
    Certification(
      certification = "TV-Y",
      order = 1,
      meaning = UIText.ResourceText(Res.string.tv_y),
    ),
    Certification(
      certification = "TV-Y7",
      order = 2,
      meaning = UIText.ResourceText(Res.string.tv_y7),
    ),
    Certification(
      certification = "TV-G",
      order = 3,
      meaning = UIText.ResourceText(Res.string.tv_g),
    ),
    Certification(
      certification = "TV-PG",
      order = 4,
      meaning = UIText.ResourceText(Res.string.tv_pg),
    ),
    Certification(
      certification = "TV-14",
      order = 5,
      meaning = UIText.ResourceText(Res.string.tv_14),
    ),
    Certification(
      certification = "TV-MA",
      order = 6,
      meaning = UIText.ResourceText(Res.string.tv_ma),
    ),
  )
}
