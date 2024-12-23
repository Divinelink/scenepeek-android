package com.divinelink.core.fixtures.details.person

import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender

object PersonDetailsFactory {

  const val BIOGRAPHY = "Steven John Carell (born August 16, 1962) is an American actor" +
    " and comedian. He played Michael Scott in The Office (2005–2011; 2013), NBC’s adaptation of " +
    "the British series created by Stephen Merchant and Ricky Gervais, " +
    "where Carell also worked as an occasional producer, writer and director. " +
    "Carell has received numerous accolades for his performances in both film and television," +
    " including the Golden Globe Award for Best Actor – Television Series Musical or Comedy" +
    " for his work on The Office. He was recognized as \"America's Funniest Man\" " +
    "by Life magazine.\n\nCarell gained recognition as a correspondent on The Daily " +
    "Show with Jon Stewart from 1999 to 2005. He went on to star in several comedy films, " +
    "including Anchorman: The Legend of Ron Burgundy (2004) and its 2013 sequel, as well " +
    "as The 40-Year-Old Virgin (2005), Evan Almighty (2007), Get Smart (2008), Date Night " +
    "(2010), Crazy, Stupid, Love (2011), and The Way, Way Back (2013). He also voice " +
    "acted in Over the Hedge (2006), Horton Hears a Who! (2008) and the Despicable " +
    "Me franchise (2010–present).\n\nCarell began to shift into more dramatic " +
    "roles in the 2010s, with his role as wrestling coach and convicted murderer " +
    "John Eleuthère du Pont in the drama film Foxcatcher (2014) earning him, among various" +
    " honors, nominations for the Academy Award for Best Actor, the Golden Globe" +
    " Award for Best Actor – Motion Picture Drama and the BAFTA Award for Best Actor " +
    "in a Supporting Role. He also starred in Little Miss Sunshine (2006)," +
    " The Big Short (2015), and Battle of the Sexes (2017), the last two " +
    "earning him his eighth and ninth Golden Globe Award nominations, respectively. " +
    "In 2018, he re-teamed with Anchorman and The Big Short director Adam McKay" +
    " for the Dick Cheney biographical film Vice, in which he portrayed Donald " +
    "Rumsfeld, and played journalist David Sheff in the drama film Beautiful" +
    " Boy.\n\nCarell returned to television as the co-creator of the TBS comedy " +
    "series Angie Tribeca (2016–2018), which he developed with his wife, " +
    "Nancy Carell. He starred as Mitch Kessler in the Apple TV+ drama series The" +
    " Morning Show (2019–present), for which he received a nomination for the " +
    "Primetime Emmy Award for Outstanding Lead Actor in a Drama Series. He also " +
    "returned to comedy with the lead role of General Mark R. Naird in the" +
    " Netflix sitcom Space Force (2020–present)."

  fun steveCarell() = PersonDetails(
    person = Person(
      id = 4495,
      name = "Steve Carell",
      profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
      gender = Gender.MALE,
      knownForDepartment = "Acting",
      role = listOf(PersonRole.Unknown),
    ),
    biography = BIOGRAPHY,
    birthday = "1962-08-16",
    deathday = null,
    placeOfBirth = "Concord, Massachusetts, USA",
    homepage = null,
    alsoKnownAs = listOf(
      "Steven Carrel",
      "Steven Carel ",
      "Steve Carel ",
      "Steven Carell",
      "Стив Карелл",
      "ستيف كارل",
      "スティーヴ・カレル",
      "스티브 커렐",
      "สตีฟ คาเรล",
      "史提夫·加維",
      "Steven John \"Steve\" Carell",
      "Steven John Carell",
      "Στίβεν Τζον Καρέλ",
      "Στίβεν Καρέλ",
      "סטיב קארל",
      "Стів Карелл",
    ),
    imdbId = "nm0136797",
    popularity = 77.108,
    insertedAt = ClockFactory.augustFifteenth2021().now().epochSeconds.toString(),
  )

  class PersonDetailsWzd(private var personDetails: PersonDetails) {

    fun withKnownForDepartment(knownForDepartment: String?) = apply {
      personDetails = personDetails.copy(
        person = personDetails.person.copy(knownForDepartment = knownForDepartment),
      )
    }

    fun create() = personDetails
  }

  fun PersonDetails.toWzd(block: PersonDetailsWzd.() -> Unit) =
    PersonDetailsWzd(this).apply(block).create()
}
