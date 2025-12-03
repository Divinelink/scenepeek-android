package com.divinelink.core.testing.factories.model.change

import com.divinelink.core.model.change.ChangeAction
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

object PersonChangeItemSample {

  fun biography() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("Alexandros Karpas is a Greek YouTuber"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:47:04 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue(
        "Alexandros Karpas is a Greek YouTuber, filmmaker," +
          " and co-founder of the multimedia company Unboxholics",
      ),
      originalValue = StringValue("Alexandros Karpas is a Greek YouTuber"),
    ),
  )

  fun gender() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("0"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd72",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:47:04 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("2"),
      originalValue = StringValue("0"),
    ),
  )

  fun birthday() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("1986-12-01"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("1986-12-04"),
      originalValue = StringValue("1986-12-01"),
    ),
  )

  fun deathday() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("2024-09-06"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("2024-09-10"),
      originalValue = StringValue("2024-09-06"),
    ),
  )

  fun homepage() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("https://www.unboxholics"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("https://www.unboxholics.com"),
      originalValue = StringValue("https://www.unboxholics"),
    ),
  )

  fun imdbId() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("nm000"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("nm001"),
      originalValue = StringValue("nm000"),
    ),
  )

  fun name() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("Alexandros"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("Alexandros Karpas"),
      originalValue = StringValue("Alexandros"),
    ),
  )

  fun placeOfBirth() = listOf(
    ChangeItem(
      id = "66daae6273d2cc58165ddd76",
      action = ChangeAction.ADDED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("Aridaia"),
      originalValue = null,
    ),
    ChangeItem(
      id = "66daae6273d2cc58165ddd73",
      action = ChangeAction.UPDATED,
      time = "2024-09-06 07:25:22 UTC",
      iso6391 = "en",
      iso31661 = "US",
      value = StringValue("Aridaia, Greece"),
      originalValue = StringValue("Aridaia"),
    ),
  )

  class PersonChangeItemSampleWizard(private var items: List<ChangeItem>) {

    fun withDeleteAction() = apply {
      items = items.dropLast(1) + items.last().copy(
        action = ChangeAction.DELETED,
        value = null,
      )
    }

    fun build(): List<ChangeItem> = items
  }

  fun List<ChangeItem>.toWizard(block: PersonChangeItemSampleWizard.() -> Unit) =
    PersonChangeItemSampleWizard(this).apply(block).build()
}
