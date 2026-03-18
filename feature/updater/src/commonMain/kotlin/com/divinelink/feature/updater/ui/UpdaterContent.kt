package com.divinelink.feature.updater.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.LCEState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import com.divinelink.core.ui.resources.core_ui_cancel
import com.divinelink.core.ui.resources.core_ui_update
import com.divinelink.feature.updater.Res
import com.divinelink.feature.updater.UpdaterAction
import com.divinelink.feature.updater.UpdaterUiState
import com.divinelink.feature.updater.current_version
import com.divinelink.feature.updater.latest_version
import com.divinelink.feature.updater.ui.provider.UpdaterUiStateParameterProvider
import com.divinelink.feature.updater.update_available_description
import com.divinelink.feature.updater.update_available_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdaterContent(
  uiState: UpdaterUiState,
  action: (UpdaterAction) -> Unit,
) {
  val urlHandler = rememberUrlHandler()

  LazyColumn(
    modifier = Modifier.fillMaxWidth(),
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .padding(bottom = MaterialTheme.dimensions.keyline_32),
        style = MaterialTheme.typography.titleMedium,
        text = stringResource(Res.string.update_available_title),
        textAlign = TextAlign.Start,
      )
    }

    item {
      when (uiState.appVersion) {
        is LCEState.Content -> Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimensions.keyline_16),
          horizontalAlignment = Alignment.Start,
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Text(text = stringResource(Res.string.update_available_description))
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = MaterialTheme.dimensions.keyline_8),
            text = stringResource(
              Res.string.current_version,
              uiState.appVersion.data.currentVersion,
            ),
            textAlign = TextAlign.Center,
          )
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
              Res.string.latest_version,
              uiState.appVersion.data.latestVersion,
            ),
            textAlign = TextAlign.Center,
          )

          Row(
            modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
          ) {
            ElevatedButton(
              modifier = Modifier.weight(1f),
              onClick = { action(UpdaterAction.Dismiss) },
            ) {
              Text(text = stringResource(UiString.core_ui_cancel))
            }

            uiState.appVersion.data.installSource.updateUrl?.let { url ->
              Button(
                modifier = Modifier.weight(1f),
                onClick = { urlHandler.openUrl(url) },
              ) {
                Text(text = stringResource(UiString.core_ui_update))
              }
            }
          }
        }
        LCEState.Error -> Unit
        LCEState.Loading -> LoadingContent()
      }
    }
  }
}

@Composable
@Previews
fun UpdaterContentPreview(
  @PreviewParameter(UpdaterUiStateParameterProvider::class) state: UpdaterUiState,
) {
  PreviewLocalProvider {
    UpdaterContent(
      uiState = state,
      action = { },
    )
  }
}
