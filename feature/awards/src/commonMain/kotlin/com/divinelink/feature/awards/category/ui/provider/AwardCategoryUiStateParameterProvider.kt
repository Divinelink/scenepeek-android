package com.divinelink.feature.awards.category.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.awards.CeremonyCategoryFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.feature.awards.category.AwardCategoryUiState

class AwardCategoryUiStateParameterProvider : PreviewParameterProvider<AwardCategoryUiState> {
  override val values: Sequence<AwardCategoryUiState> = sequenceOf(
    AwardCategoryUiState.initial(
      ceremonyId = "1-best-picture",
      category = CeremonyCategoryFactory.bestPicture(),
    ).copy(
      loading = false,
      awards = mapOf(
        "38th European Film Awards (2026)" to listOf(
          LoadingUiItem(
            item = AwardNominee(
              media = MediaItemFactory.bruceAlmighty().toStub(),
              winner = true,
              winningMedia = null,
              countries = null,
            ),
            mediaState = ItemState.Data<MediaItem>(
              loading = false,
              item = MediaItemFactory.bruceAlmighty(),
            ),
          ),
          LoadingUiItem(
            item = AwardNominee(
              media = MediaItemFactory.getSmart().toStub(),
              winner = false,
              winningMedia = null,
              countries = null,
            ),
            mediaState = ItemState.Data<MediaItem>(
              loading = false,
              item = MediaItemFactory.getSmart(),
            ),
          ),
          LoadingUiItem(
            item = AwardNominee(
              media = MediaItemFactory.FightClub().toStub(),
              winner = false,
              winningMedia = null,
              countries = listOf(Country.LEBANON, Country.IRELAND),
            ),
            mediaState = ItemState.Data<MediaItem>(
              loading = false,
              item = MediaItemFactory.getSmart(),
            ),
          ),
          LoadingUiItem(
            item = AwardNominee(
              media = MediaItemFactory.FightClub().toStub(),
              winner = false,
              winningMedia = null,
              countries = listOf(Country.LEBANON, Country.IRELAND),
            ),
            mediaState = ItemState.Loading,
          ),
        ),
      ),
    ),
  )
}
