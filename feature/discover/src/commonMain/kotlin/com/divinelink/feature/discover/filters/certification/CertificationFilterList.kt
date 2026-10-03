package com.divinelink.feature.discover.filters.certification

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.certification.Certification
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.media.ContentRating
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CertificationFilterList(
  titleRes: StringResource,
  items: List<Certification>,
  isSelected: (Certification) -> Boolean,
  onItemClick: (Certification) -> Unit,
  selected: Certification?,
  modifier: Modifier = Modifier,
) {
  val state = rememberLazyListState()
  val width = rememberMaxContentRatingWidth(items)

  LaunchedEffect(Unit) {
    val selectedIndex = items.indexOf(selected)
    if (selectedIndex != -1) {
      state.animateScrollToItem(
        index = selectedIndex,
        scrollOffset = -800,
      )
    }
  }

  LazyColumn(
    state = state,
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleMedium,
      )
    }

    items(
      items = items,
      key = { it.certification },
    ) { item ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .animateItem()
          .animateContentSize()
          .clickable { onItemClick(item) }
          .padding(
            horizontal = MaterialTheme.dimensions.keyline_8,
            vertical = MaterialTheme.dimensions.keyline_12,
          ),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier = Modifier.width(width),
        ) {
          ContentRating(
            contentRating = item.certification,
          )
        }

        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.dimensions.keyline_8)
            .weight(1f),
          text = item.meaning.getString(),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          style = MaterialTheme.typography.bodyMedium,
        )

        AnimatedContent(isSelected(item)) { selected ->
          if (selected) {
            Box(modifier = Modifier.size(MaterialTheme.dimensions.keyline_32)) {
              Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.Default.Done,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
              )
            }
          } else {
            Spacer(modifier = Modifier.size(MaterialTheme.dimensions.keyline_32))
          }
        }
      }
      HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
      )
    }
  }
}

@Composable
private fun rememberMaxContentRatingWidth(labels: List<Certification>): Dp {
  val density = LocalDensity.current
  val fontSize = MaterialTheme.typography.bodySmall.fontSize
  val fontWeight = MaterialTheme.typography.titleSmall.fontWeight
  val textMeasurer = rememberTextMeasurer()

  return remember(labels, fontSize, fontWeight) {
    val textStyle = TextStyle(
      fontSize = fontSize,
      fontWeight = fontWeight,
    )

    val maxTextWidthPx = labels.maxOfOrNull { certification ->
      textMeasurer.measure(
        text = certification.certification,
        style = textStyle,
      ).size.width
    } ?: 0

    with(density) {
      val paddingAndBorder = (12.dp * 2 + 1.dp * 2)
      maxTextWidthPx.toDp() + paddingAndBorder
    }
  }
}
