package com.divinelink.core.model.certification

import com.divinelink.core.model.UIText

object ShowCertification {

  val US: List<Certification> = listOf(
    Certification(
      certification = "NR",
      order = 0,
      meaning = UIText.StringText("No rating information."),
    ),
    Certification(
      certification = "TV-Y",
      order = 1,
      meaning = UIText.StringText("Designed to be appropriate for all children, including ages 2–6."),
    ),
    Certification(
      certification = "TV-Y7",
      order = 2,
      meaning = UIText.StringText("Designed for children age 7 and above; may contain mild fantasy violence."),
    ),
    Certification(
      certification = "TV-G",
      order = 3,
      meaning = UIText.StringText("Suitable for all ages. Little or no violence, language, or sexual content."),
    ),
    Certification(
      certification = "TV-PG",
      order = 4,
      meaning = UIText.StringText("May contain material parents could find unsuitable for younger children."),
    ),
    Certification(
      certification = "TV-14",
      order = 5,
      meaning = UIText.StringText("Strongly cautioned: some material is unsuitable for children under 14."),
    ),
    Certification(
      certification = "TV-MA",
      order = 6,
      meaning = UIText.StringText("Mature audiences only. Designed for viewers 17 and up."),
    ),
  )
}
