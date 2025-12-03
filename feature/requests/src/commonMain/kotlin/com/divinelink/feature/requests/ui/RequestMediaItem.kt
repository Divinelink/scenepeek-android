package com.divinelink.feature.requests.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.coil.OpaqueBackdropImage
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.modal.jellyseerr.manage.SeasonPill
import com.divinelink.core.ui.extension.localizeIsoDate
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.resources.core_ui_season
import com.divinelink.core.ui.resources.core_ui_status
import com.divinelink.core.ui.skeleton.RequestItemSkeleton
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.resources.Res
import com.divinelink.feature.requests.resources.feature_requests_profile
import com.divinelink.feature.requests.resources.feature_requests_request_by
import com.divinelink.feature.requests.resources.feature_requests_requested
import com.divinelink.feature.requests.ui.buttons.ApprovedActionButtons
import com.divinelink.feature.requests.ui.buttons.DeclinedActionButtons
import com.divinelink.feature.requests.ui.buttons.FailedActionButtons
import com.divinelink.feature.requests.ui.buttons.PendingActionButtons
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LazyItemScope.RequestMediaItem(
  modifier: Modifier = Modifier,
  canManageRequest: Boolean,
  canRequestAdvanced: Boolean,
  item: RequestUiItem,
  onAction: (RequestsAction) -> Unit,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  when (val state = item.mediaState) {
    is ItemState.Data -> Card(
      modifier = modifier
        .animateItem()
        .combinedClickable(
          onClick = { onClick(state.item) },
          onLongClick = { onLongClick(state.item) },
        )
        .fillMaxWidth(),
    ) {
      Box {
        OpaqueBackdropImage(path = state.item.backdropPath)

        Column(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            MediaImage(
              media = state.item,
              modifier = Modifier
                .align(Alignment.Top)
                .height(MaterialTheme.dimensions.keyline_96),
            )

            Column(
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              Text(
                text = state.item.releaseDate.take(4),
                style = MaterialTheme.typography.titleSmall,
              )
              Text(text = state.item.name, style = MaterialTheme.typography.titleMedium)

              if (item.request.seasons.isNotEmpty()) {
                val seasons = item.request.seasons
                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimensions.keyline_8,
                  ),
                  verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
                  itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                  Text(
                    text = pluralStringResource(UiPlurals.core_ui_season, seasons.size),
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )

                  seasons.forEach { season ->
                    SeasonPill(season.seasonNumber)
                  }
                }
              }
            }
          }

          Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              text = stringResource(UiString.core_ui_status),
            )

            JellyseerrStatusPill(status = item.request.status)
          }

          Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Text(
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              text = stringResource(Res.string.feature_requests_requested),
            )
            item.request.requestDate.localizeIsoDate()?.let { date ->
              Text(
                text = stringResource(
                  Res.string.feature_requests_request_by,
                  date,
                  item.request.requester.displayName,
                ),
                style = MaterialTheme.typography.bodyMedium,
              )
            }
          }

          item.request.profileName?.let { profileName ->
            ProfileNameSection(profileName)
          }

          ActionButtons(
            request = item.request,
            enabled = !state.loading,
            onAction = onAction,
            canRequestAdvanced = canRequestAdvanced,
            canManageRequest = canManageRequest,
          )

          if (state.loading) {
            LinearProgressIndicator(
              modifier = Modifier
                .testTag(TestTags.LINEAR_LOADING_INDICATOR)
                .fillMaxWidth(),
            )
          }
        }
      }
    }

    ItemState.Loading -> RequestItemSkeleton(
      modifier = modifier,
    )

    null -> {
      // Do nothing
    }
  }
}

@Composable
private fun ProfileNameSection(profileName: String) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      text = stringResource(Res.string.feature_requests_profile),
    )
    Text(
      text = profileName,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Composable
fun ActionButtons(
  request: JellyseerrRequest,
  enabled: Boolean,
  canManageRequest: Boolean,
  canRequestAdvanced: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  when (request.requestStatus) {
    JellyseerrStatus.Request.PENDING -> PendingActionButtons(
      canManageRequests = canManageRequest,
      canRequestAdvanced = canRequestAdvanced,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )
    JellyseerrStatus.Request.APPROVED -> ApprovedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )

    JellyseerrStatus.Request.DECLINED -> DeclinedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )

    JellyseerrStatus.Request.FAILED -> FailedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )
    JellyseerrStatus.Request.UNKNOWN -> {
      // Do nothing
    }
  }
}
