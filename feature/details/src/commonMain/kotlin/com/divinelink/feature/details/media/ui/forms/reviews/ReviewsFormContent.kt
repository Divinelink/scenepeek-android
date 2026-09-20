package com.divinelink.feature.details.media.ui.forms.reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.media.DetailSection
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.list.ScrollableMediaContent
import com.divinelink.feature.details.media.ui.DetailsSectionViewModel
import com.divinelink.feature.details.media.ui.components.DetailFormLoading
import com.divinelink.feature.details.media.ui.section.DetailSectionData
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_no_recommendation_available
import com.divinelink.feature.details.resources.feature_details_no_recommendations_available_desc
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

//@Composable
//fun SectionFormContent(
//  modifier: Modifier = Modifier,
//  section: DetailSection,
//  viewModel: DetailsSectionViewModel = koinViewModel { parametersOf(section) },
//  title: String,
//) {
//  val state by viewModel.uiState.collectAsStateWithLifecycle()
//
//  when (val data = state.data) {
//    is DetailSectionData.Error -> Column(
//      modifier = Modifier
//        .fillMaxHeight()
//        .padding(vertical = MaterialTheme.dimensions.keyline_32)
//        .verticalScroll(rememberScrollState()),
//    ) {
//      BlankSlate(uiState = data.state)
//    }
//    DetailSectionData.Loading -> DetailFormLoading()
//    is DetailSectionData.Reviews -> Unit
//    is DetailSectionData.Media -> ScrollableMediaContent(
//      modifier = modifier.testTag(TestTags.Details.Recommendations.FORM),
//      items = data.media,
//      section = ViewableSection.MEDIA_DETAILS,
//      onLoadMore = {},
//      onSwitchPreferences = onSwitchPreferences,
//      onClick = onItemClick,
//      onLongClick = onLongClick,
//      canLoadMore = false,
//      emptyContent = {
//        BlankSlate(
//          modifier = Modifier
//            .testTag(TestTags.Details.Recommendations.EMPTY)
//            .padding(top = MaterialTheme.dimensions.keyline_12),
//          uiState = BlankSlateState.Custom(
//            title = UIText.ResourceText(Res.string.feature_details_no_recommendation_available),
//            description = UIText.ResourceText(
//              Res.string.feature_details_no_recommendations_available_desc,
//              title,
//            ),
//          ),
//        )
//      },
//    )
//  }
//
////  when (val data = state.data) {
////    is DetailsSectionForm.Error -> Column(
////      modifier = Modifier
////        .fillMaxHeight()
////        .padding(vertical = MaterialTheme.dimensions.keyline_16)
////        .verticalScroll(rememberScrollState()),
////    ) {
////      BlankSlate(
////        uiState = when (data) {
////          DetailsSectionForm.Error.Network -> BlankSlateState.Offline
////          DetailsSectionForm.Error.Unknown -> BlankSlateState.Contact
////        },
////      )
////    }
////    DetailsSectionForm.Loading -> DetailFormLoading()
//////    is DetailsSectionForm.Media<*> -> ScrollableMediaContent(
//////      modifier = modifier.testTag(TestTags.Details.Recommendations.FORM),
//////      items = (state.data as DetailsSectionForm.Media).media,
//////      section = ViewableSection.MEDIA_DETAILS,
//////      onLoadMore = {},
//////      onSwitchPreferences = onSwitchPreferences,
//////      onClick = onItemClick,
//////      onLongClick = onLongClick,
//////      canLoadMore = false,
//////      emptyContent = {
//////        BlankSlate(
//////          modifier = Modifier
//////            .testTag(TestTags.Details.Recommendations.EMPTY)
//////            .padding(top = MaterialTheme.dimensions.keyline_12),
//////          uiState = BlankSlateState.Custom(
//////            title = UIText.ResourceText(Res.string.feature_details_no_recommendation_available),
//////            description = UIText.ResourceText(
//////              Res.string.feature_details_no_recommendations_available_desc,
//////              title,
//////            ),
//////          ),
//////        )
//////      },
//////    )
////  }
//
//}
