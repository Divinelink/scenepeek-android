package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.list.ListData
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.DisplayMessageSection
import com.divinelink.core.ui.components.Material3CircularProgressIndicator
import com.divinelink.feature.add.to.account.list.AddToListAction
import com.divinelink.feature.add.to.account.list.AddToListUiState
import com.divinelink.feature.add.to.account.list.ui.provider.AddToListUiStateParameterProvider
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun AddToListContent(
  uiState: AddToListUiState,
  action: (AddToListAction) -> Unit,
) {
  Column {
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

    AnimatedVisibility(
      modifier = Modifier.fillMaxWidth(),
      visible = uiState.displayMessage != null,
    ) {
      DisplayMessageSection(
        message = uiState.displayMessage,
        onTimeout = { action(AddToListAction.ConsumeDisplayMessage) },
      )
    }

    when {
      uiState.error != null -> BlankSlate(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .padding(bottom = LocalBottomNavigationPadding.current),
        uiState = uiState.error,
        onRetry = {
          if (uiState.error is BlankSlateState.Unauthenticated) {
            action(AddToListAction.Login)
          } else {
            // Do nothing
          }
        },
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

      uiState.lists is ListData.Data -> ListsDataContent(
        data = uiState.lists.data,
        addedToLists = uiState.addedToLists,
        action = action,
      )
    }
  }
}

@Composable
@Previews
private fun AddToListContentPreview(
  @PreviewParameter(AddToListUiStateParameterProvider::class) state: AddToListUiState,
) {
  AppTheme {
    Surface {
      AddToListContent(
        uiState = state,
        action = { },
      )
    }
  }
}
