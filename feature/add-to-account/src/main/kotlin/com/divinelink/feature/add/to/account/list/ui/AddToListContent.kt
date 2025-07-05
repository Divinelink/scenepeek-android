package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.list.ListData
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.Material3CircularProgressIndicator
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.AddToListUiState
import com.divinelink.feature.add.to.account.list.AddToListUserInteraction

@Composable
fun AddToListContent(
  uiState: AddToListUiState,
  userInteraction: (AddToListUserInteraction) -> Unit,
) {
  AnimatedVisibility(
    visible = uiState.isLoading && uiState.lists is ListData.Data,
    modifier = Modifier.fillMaxWidth(),
  ) {
    LinearProgressIndicator(
      modifier = Modifier
        .testTag(TestTags.LINEAR_LOADING_INDICATOR)
        .fillMaxWidth(),
    )
  }

  when {
    uiState.error != null -> BlankSlate(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .padding(bottom = LocalBottomNavigationPadding.current),
      uiState = uiState.error,
    )

    uiState.lists is ListData.Initial -> Box(
      modifier = Modifier
        .testTag(TestTags.LOADING_CONTENT)
        .fillMaxWidth()
        .height(MaterialTheme.dimensions.keyline_96),
    ) {
      Material3CircularProgressIndicator(
        modifier = Modifier
          .wrapContentSize()
          .align(Alignment.Center),
      )
    }

    uiState.lists is ListData.Data && uiState.lists.isEmpty -> Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.dimensions.keyline_32),
      text = stringResource(R.string.feature_add_to_account_empty_lists),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyLarge,
    )

    uiState.lists is ListData.Data -> ListsDataContent(
      data = uiState.lists.data,
      userInteraction = userInteraction,
    )
  }
}
