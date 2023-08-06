@file:Suppress("LongMethod")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.DescriptionAttrs
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.getString
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.SearchBarSize
import com.andreolas.movierama.ui.theme.dimensions
import com.andreolas.movierama.ui.theme.keyline_negative8

const val SEARCH_BAR_LOADING_INDICATOR_TAG = "SEARCH_BAR_LOADING_INDICATOR"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieRamaSearchBar(
  modifier: Modifier = Modifier,
  query: String? = null,
  onClearClicked: () -> Unit,
  onSearchFieldChanged: (String) -> Unit,
  actions: @Composable RowScope.() -> Unit = {},
  state: ToolbarState = ToolbarState.Unfocused,
  isLoading: Boolean = false,
  scrollBehavior: TopAppBarScrollBehavior? = null,
) {
  val toolbarState = remember { mutableStateOf(state) }
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(toolbarState.value) {
    if (toolbarState.value == ToolbarState.Focused) {
      focusRequester.requestFocus()
    }
  }

  TopAppBar(
    modifier = modifier,
    scrollBehavior = scrollBehavior,
    colors = TopAppBarDefaults.topAppBarColors(
      scrolledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    navigationIcon = {
      // Do nothing
    },
    title = {
      // Add offset to center the title in the app bar
      Box(Modifier.offset(x = MaterialTheme.dimensions.keyline_negative8)) {
        Row(
          modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
          verticalAlignment = Alignment.CenterVertically
        ) {

          Crossfade(
            targetState = toolbarState.value,
            label = DescriptionAttrs.AnimationLabels.SearchBarCrossfade,
          ) { toolbar ->
            if (toolbar == ToolbarState.Focused || query?.isNotEmpty() == true) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .weight(1f)
                  .height(SearchBarSize),
              ) {
                SearchIconWithLoading(
                  isLoading = isLoading,
                  onClearClicked = onClearClicked,
                  toolbarState = toolbarState,
                )

                FocusedSearchField(
                  value = query,
                  onSearchFieldChanged = onSearchFieldChanged,
                  modifier = Modifier
                    .focusRequester(focusRequester)
                )
              }
            } else {
              Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .height(SearchBarSize)
                  .fillMaxWidth(TEXT_MAX_WIDTH)
                  .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                  ) {
                    toolbarState.value = when (toolbarState.value) {
                      ToolbarState.Focused -> ToolbarState.Unfocused
                      ToolbarState.Unfocused -> ToolbarState.Focused
                    }
                  }
              ) {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = stringResource(id = R.string.toolbar_search),
                  modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
                )
                Text(
                  overflow = TextOverflow.Ellipsis,
                  text = stringResource(id = R.string.toolbar_search),
                  style = MaterialTheme.typography.bodyLarge,
                )
              }
            }
          }
          Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
          )
        }
      }
    },
  )
}

@Composable
private fun SearchIconWithLoading(
  isLoading: Boolean,
  onClearClicked: () -> Unit,
  toolbarState: MutableState<ToolbarState>,
) {
  when (isLoading) {
    true -> {
      Material3CircularProgressIndicator(
        modifier = Modifier
          .testTag(SEARCH_BAR_LOADING_INDICATOR_TAG)
          .padding(
            start = MaterialTheme.dimensions.keyline_16,
            end = MaterialTheme.dimensions.keyline_10
          )
          .padding(horizontal = MaterialTheme.dimensions.keyline_8)
          .size(MaterialTheme.dimensions.keyline_24)
      )
    }
    false -> {
      IconButton(
        modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_2),
        onClick = {
          onClearClicked()
          toolbarState.value = ToolbarState.Unfocused
        },
      ) {
        Icon(
          imageVector = Icons.Default.Clear,
          contentDescription = stringResource(id = R.string.clear_search_button_content_description),
        )
      }
    }
  }
}

const val SEARCH_INPUT_FIELD_TAG = "SEARCH_INPUT_FIELD"

@Composable
private fun FocusedSearchField(
  modifier: Modifier = Modifier,
  value: String? = null,
  onSearchFieldChanged: (String) -> Unit,
) {
  val searchContentDescription = stringResource(R.string.toolbar_search_placeholder)

  BasicTextField(
    modifier = modifier
      .semantics { contentDescription = searchContentDescription }
      .fillMaxWidth(TEXT_MAX_WIDTH)
      .padding(end = MaterialTheme.dimensions.keyline_12),
    value = value ?: "",
    textStyle = TextStyle(
      fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
      fontSize = MaterialTheme.typography.bodyMedium.fontSize,
      color = MaterialTheme.colorScheme.onSurface,
    ),
    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions.Default,
    onValueChange = onSearchFieldChanged,
    singleLine = true,
    decorationBox = { innerTextField ->
      if (value.isNullOrEmpty()) {
        Text(
          maxLines = 1,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          overflow = TextOverflow.Ellipsis,
          text = UIText.ResourceText(R.string.toolbar_search_placeholder).getString()
        )
      }
      innerTextField()
    }
  )
}

enum class ToolbarState {
  Focused,
  Unfocused,
}

private const val TEXT_MAX_WIDTH = 0.84F

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SearchBarPreview() {
  AppTheme {
    Surface {
      MovieRamaSearchBar(
        actions = {},
        onClearClicked = {},
        onSearchFieldChanged = {},
      )
    }
  }
}

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun FocusedSearchBarPreview() {
  AppTheme {
    Surface {
      MovieRamaSearchBar(
        state = ToolbarState.Focused,
        actions = {},
        onClearClicked = {},
        onSearchFieldChanged = {},
      )
    }
  }
}

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun FilledSearchBarPreview() {
  AppTheme {
    Surface {
      MovieRamaSearchBar(
        state = ToolbarState.Focused,
        query = "Flight Club",
        actions = {
          IconButton(
            onClick = {},
          ) {
            Icon(Icons.Filled.Settings, null)
          }
        },
        onClearClicked = {},
        onSearchFieldChanged = {},
        isLoading = true,
      )
    }
  }
}
