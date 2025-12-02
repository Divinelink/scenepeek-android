package com.divinelink.core.testing.factories.model.change

import com.divinelink.core.model.change.ChangeAction
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

object PersonChangesSample {

  fun biography() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue(
        "Alexandros Karpas is a Greek YouTuber, filmmaker, and co-founder of the " +
          "multimedia company Unboxholics.",
      ),
      originalValue = null,
    ),
    ChangeItem(
      id = "66dab3782b4e67af085dde2c",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:47:04 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue(
        "Alexandros Karpas is a Greek YouTuber, filmmaker, and co-founder of the multimedia " +
          "company Unboxholics, where he serves as the art director. He is also known for " +
          "his work as a director and cinematographer, having directed and edited the 2024" +
          " horror film \"Don't Open The Door\", the short film \"Lurking Near\" in 2017 " +
          "and the 2015 TV documentary \"Volta stin Aridaia\".\n" +
          "\n" +
          "He has a strong interest in horror-themed photography and often shares his creative" +
          " work with his substantial Instagram following of over 300,000 fans." +
          " Alongside his twin brother, Sakis Karpas, Alexandros has built a prominent" +
          " online presence through YouTube and Twitch, where they have gained a significant" +
          " audience\u200B.",
      ),
      originalValue = StringValue(
        "Alexandros Karpas is a Greek YouTuber, filmmaker, and co-founder of the multimedia" +
          " company Unboxholics.",
      ),
    ),
  )

  fun tiktokId() = listOf(
    ChangeItem(
      id = "66dab0564aac0bc5c3ea9b87",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:33:42 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("discover"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66dab113acf03a1334de0424",
      action = ChangeAction.DELETED,
      time = "2024-09-06 07:36:51 UTC",
      iso6391 = "",
      iso31661 = "",
      value = null,
      originalValue = StringValue("discover"),
    ),
    ChangeItem(
      id = "66dab19ca91061584121b778",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:39:08 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("alexandros-karpas"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66dab23fd49ec36b1a34d0ef",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:41:51 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("unboxholics"),
      originalValue = StringValue("alexandros-karpas"),
    ),
  )

  fun alsoKnownAs() = emptyList<ChangeItem>()

  fun youtubeId() = listOf(
    ChangeItem(
      id = "66dab0564aac0bc5c3ea9b8a",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:33:42 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("user"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66dab141bf788966913c65c2",
      action = ChangeAction.DELETED,
      time = "2024-09-06 07:37:37 UTC",
      iso6391 = "",
      iso31661 = "",
      value = null,
      originalValue = StringValue("user"),
    ),
    ChangeItem(
      id = "66dab19da91061584121b77b",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:39:09 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("@Unboxholics"),
      originalValue = null,
    ),
  )

  fun facebookId() = listOf(
    ChangeItem(
      id = "66dab19ca91061584121b777",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:39:08 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("alexandroskarpas"),
      originalValue = null,
    ),
  )

  fun name() = listOf(
    ChangeItem(
      id = "66db014a0b6801c5793c69b2",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 13:19:06 UTC",
      iso6391 = "el",
      iso31661 = "GR",
      value = StringValue("Αλέξανδρος Καρπάς"),
      originalValue = StringValue("Alexandros Karpas"),
    ),
  )

  fun primary() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd75",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("false"),
      originalValue = null,
    ),
  )

  fun imdbId() = listOf(
    ChangeItem(
      id = "66dab0564aac0bc5c3ea9b83",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:33:42 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("nm8874716"),
      originalValue = null,
    ),
  )

  fun instagramId() = listOf(
    ChangeItem(
      id = "66dab141bf788966913c65c1",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:37:37 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("alexandroskarpas"),
      originalValue = null,
    ),
  )

  fun twitterId() = listOf(
    ChangeItem(
      id = "66dab19ca91061584121b77a",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:39:08 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("voncroy_alex"),
      originalValue = null,
    ),
  )

  fun birthday() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd72",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("1986-12-04"),
      originalValue = null,
    ),
  )

  fun placeOfBirth() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd74",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("Aridaia, Macedonia, Greece [now Central Macedonia, Greece]"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daafb639cc7603de25634f",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:31:02 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("Aridaia, Central Macedonia, Greece"),
      originalValue = StringValue("Aridaia, Macedonia, Greece [now Central Macedonia, Greece]"),
    ),
  )

  fun gender() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "",
      iso31661 = "",
      value = StringValue("2"),
      originalValue = StringValue("0"),
    ),
  )
}
