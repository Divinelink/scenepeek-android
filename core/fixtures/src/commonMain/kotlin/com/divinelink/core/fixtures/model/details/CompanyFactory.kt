package com.divinelink.core.fixtures.model.details

import com.divinelink.core.model.details.Company
import com.divinelink.core.model.locale.Country

object CompanyFactory {

  val fox2000Pictures = Company(
    id = 711,
    name = "Fox 2000 Pictures",
    logoPath = "/tEiIH5QesdheJmDAqQwvtN60727.png",
    country = Country.UNITED_STATES,
  )

  val regencyEnterprises = Company(
    id = 508,
    logoPath = "/4sGWXoboEkWPphI6es6rTmqkCBh.png",
    name = "Regency Enterprises",
    country = Country.UNITED_STATES,
  )

  val linsonEntertainment = Company(
    id = 4700,
    logoPath = "/A32wmjrs9Psf4zw0uaixF0GXfxq.png",
    name = "Linson Entertainment",
    country = Country.UNITED_STATES,
  )

  val twentiethCenturyFox = Company(
    id = 25,
    logoPath = "/nM2MfoMqzJQRiSynsDabOtFKetD.png",
    name = "20th Century Fox",
    country = Country.UNITED_STATES,
  )

  val taurusFilm = Company(
    id = 20555,
    logoPath = "/hD8yEGUBlHOcfHYbujp71vD8gZp.png",
    name = "Taurus Film",
    country = Country.GERMANY,
  )

  val universalTelevision = Company(
    id = 26727,
    logoPath = "/jeTxdjXhzgKZyLr3l9MllkTn3fy.png",
    name = "Universal Television",
    country = Country.UNITED_STATES,
  )
  val deedleDeeProductions = Company(
    id = 59845,
    logoPath = "",
    name = "Deedle-Dee Productions",
    country = Country.UNITED_STATES,
  )
  val reveilleProductions = Company(
    id = 6383,
    logoPath = "/bQVIn5JphAADvT4hljIMuzgm3sz.png",
    name = "Reveille Productions",
    country = Country.UNITED_STATES,
  )
  val shineAmerica = Company(
    id = 215547,
    logoPath = "/bLfxRVYgDEFMPI5G2Kwezw3YsOr.png",
    name = "Shine America",
    country = Country.UNITED_STATES,
  )
  val universalMediaStudios = Company(
    id = 8301,
    logoPath = "/zrcVDabl14MNfPwxL8DC2IyR12t.png",
    name = "Universal Media Studios",
    country = Country.UNITED_STATES,
  )

  val fightClub = listOf(
    fox2000Pictures,
    regencyEnterprises,
    linsonEntertainment,
    twentiethCenturyFox,
    taurusFilm,
  )

  val theOffice = listOf(
    universalTelevision,
    deedleDeeProductions,
    reveilleProductions,
    shineAmerica,
    universalMediaStudios,
  )
}
