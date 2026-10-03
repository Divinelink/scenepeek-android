package com.divinelink.core.model.certification

import com.divinelink.core.model.UIText

object MovieCertification {

  val US: List<Certification> = listOf(
    Certification(
      certification = "NR",
      meaning = UIText.StringText("No rating information available."),
      order = 0,
    ),
    Certification(
      certification = "G",
      meaning = UIText.StringText("All ages admitted."),
      order = 1,
    ),
    Certification(
      certification = "PG",
      meaning = UIText.StringText(
        "Some material may not be suitable for children. " +
          "Parents should give guidance.",
      ),
      order = 2,
    ),
    Certification(
      certification = "PG-13",
      meaning = UIText.StringText("Some material may be inappropriate for children under 13."),
      order = 3,
    ),
    Certification(
      certification = "R",
      meaning = UIText.StringText(
        "Under 17 requires an accompanying parent or adult guardian. " +
          "Contains some adult material.",
      ),
      order = 4,
    ),
    Certification(
      certification = "NC-17",
      meaning = UIText.StringText("Adults only. No one 17 and under admitted."),
      order = 5,
    ),
  )
}
