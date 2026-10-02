package com.divinelink.core.model.ui

import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.sort.SortContext

sealed interface ViewableSection {
  val key: String

  data object PersonCredits : ViewableSection {
    override val key: String = "person_credits"
  }

  data object MediaDetails : ViewableSection {
    override val key = "media_details"
  }

  data object Lists : ViewableSection {
    override val key = "lists"
  }

  data object Search : ViewableSection {
    override val key = "search"
  }

  companion object {
    val defaultEntries: List<ViewableSection> = listOf(
      PersonCredits,
      MediaDetails,
      Lists,
      Search,
      SortableSection.ListDetails,
    )
    val allSections: List<ViewableSection> by lazy {
      defaultEntries + SortableSection.all
    }
  }
}

sealed interface SortableSection : ViewableSection {
  val sortContext: SortContext

  data object ListDetails : SortableSection {
    override val key = "list_details"
    override val sortContext = SortContext.ListDetails
  }

  data object UserData : SortableSection {
    override val key = "user_data"
    override val sortContext = SortContext.UserData
  }

  sealed interface Discover : SortableSection {
    val type: DiscoverType

    data class Movies(override val type: DiscoverType) : Discover {
      override val key: String = "${type.value}_movies"
      override val sortContext: SortContext = when (type) {
        DiscoverType.Discover -> SortContext.DiscoverMovie
        DiscoverType.Upcoming -> SortContext.UpcomingMovie
      }
    }

    data class Shows(override val type: DiscoverType) : Discover {
      override val key: String = "${type.value}_shows"
      override val sortContext: SortContext = when (type) {
        DiscoverType.Discover -> SortContext.DiscoverShow
        DiscoverType.Upcoming -> SortContext.UpcomingShow
      }
    }
  }

  companion object {
    val all: List<SortableSection> by lazy {
      listOf(UserData) + DiscoverType.entries.flatMap { entryPoint ->
        listOf(
          Discover.Movies(entryPoint),
          Discover.Shows(entryPoint),
        )
      }
    }
  }
}
