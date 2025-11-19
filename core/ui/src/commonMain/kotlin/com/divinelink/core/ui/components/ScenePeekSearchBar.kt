@file:OptIn(ExperimentalMaterial3Api::class)

package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.SearchBarShape
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.clear_search_button_content_description
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.core_ui_movies
import com.divinelink.core.ui.core_ui_people
import com.divinelink.core.ui.core_ui_toolbar_search
import com.divinelink.core.ui.core_ui_toolbar_search_placeholder
import com.divinelink.core.ui.core_ui_tv_shows
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.text.BasicTextFieldWithCursorAtEnd
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePeekSearchBar(
  modifier: Modifier = Modifier,
  query: String? = null,
  onClearClicked: () -> Unit,
  onSearchFieldChanged: (String) -> Unit,
  actions: @Composable RowScope.() -> Unit = {},
  state: ToolbarState = ToolbarState.Unfocused,
  focusTrigger: Int = 0,
  isLoading: Boolean = false,
  isSearchable: Boolean = true,
  onFocused: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior? = null,
) {
  var toolbarState by remember { mutableStateOf(state) }
  val focusRequester = remember { FocusRequester() }
  val focusManager = LocalFocusManager.current
  val keyboardController = LocalSoftwareKeyboardController.current

  // Sync external state changes to local state
  LaunchedEffect(state) {
    toolbarState = state
  }

  LaunchedEffect(focusTrigger) {
    if (focusTrigger > 0 && isSearchable) {
      // Always request focus and show keyboard when trigger changes
      focusRequester.requestFocus()
      delay(50)
      keyboardController?.show()
    }
  }

  LaunchedEffect(toolbarState) {
    if (toolbarState == ToolbarState.Focused) {
      if (isSearchable) {
        focusRequester.requestFocus()
      } else {
        onFocused()
      }
    } else {
      focusManager.clearFocus()
    }
  }

  val typeOptions = listOf(
    UiString.core_ui_movies,
    UiString.core_ui_tv_shows,
    UiString.core_ui_people,
  )

  var typeIndex by rememberSaveable { mutableIntStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(3500)

      typeIndex = (typeIndex + 1) % typeOptions.size
    }
  }

  TopAppBar(
    modifier = modifier.testTag(TestTags.Components.SearchBar.SEARCH_BAR.format(toolbarState.name)),
    scrollBehavior = scrollBehavior,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.Transparent,
      scrolledContainerColor = Color.Transparent,
    ),
    title = {
      // Add offset to center the title in the app bar
      Box(modifier = Modifier.offset(x = -MaterialTheme.dimensions.keyline_8)) {
        Row(
          modifier = modifier
            .clip(SearchBarShape)
            .conditional(
              condition = isSearchable,
              ifFalse = {
                Modifier
                  .testTag(TestTags.Components.SearchBar.CLICKABLE_SEARCH_BAR)
                  .clickable { onFocused() }
              },
            )
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Crossfade(
            targetState = toolbarState,
            label = "SearchBar Crossfade",
          ) { toolbar ->
            if (toolbar == ToolbarState.Focused) {
              SearchIconWithLoading(
                isLoading = isLoading,
                onClearClicked = {
                  if (query?.isNotEmpty() == true) {
                    onSearchFieldChanged("")
                  } else {
                    onClearClicked()
                  }
                  toolbarState = ToolbarState.Unfocused
                },
              )
            } else {
              IconButton(onClick = { toolbarState = ToolbarState.Focused }) {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = stringResource(UiString.core_ui_toolbar_search),
                )
              }
            }
          }

          if (isSearchable) {
            SearchField(
              modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                  toolbarState = if (focusState.isFocused) {
                    ToolbarState.Focused
                  } else {
                    ToolbarState.Unfocused
                  }
                }
                .focusRequester(focusRequester),
              value = query,
              onQueryChanged = onSearchFieldChanged,
              typeOptions = typeOptions,
              typeIndex = typeIndex,
            )
          } else {
            SearchPlaceHolder(
              typeOptions = typeOptions,
              typeIndex = typeIndex,
            )
            Spacer(modifier = Modifier.weight(1f))
          }

          Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = {
              AnimatedVisibility(query?.isNotEmpty() == true) {
                IconButton(
                  onClick = { onSearchFieldChanged("") },
                ) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(UiString.core_ui_toolbar_search),
                  )
                }
              }

              actions()
            },
          )
        }
      }
    },
  )
}

@Composable
private fun SearchPlaceHolder(
  modifier: Modifier = Modifier,
  typeOptions: List<StringResource>,
  typeIndex: Int,
) {
  Row(modifier = modifier) {
    Text(
      text = stringResource(UiString.core_ui_toolbar_search),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
      text = " ",
      style = MaterialTheme.typography.titleMedium,
    )

    Row(modifier) {
      ScrollingText(
        text = stringResource(typeOptions[typeIndex]),
      )
    }
  }
}

@Composable
private fun SearchIconWithLoading(
  isLoading: Boolean,
  onClearClicked: () -> Unit,
) {
  AnimatedContent(
    targetState = isLoading,
    label = "SearchIconWithLoading",
  ) { loading ->
    when (loading) {
      true -> Material3CircularProgressIndicator(
        modifier = Modifier
          .testTag(TestTags.Components.SearchBar.LOADING_INDICATOR)
          .padding(
            horizontal = MaterialTheme.dimensions.keyline_12,
            vertical = MaterialTheme.dimensions.keyline_8,
          )
          .size(MaterialTheme.dimensions.keyline_24),
      )
      false -> IconButton(
        modifier = Modifier.testTag(TestTags.Components.SearchBar.CLOSE_SEARCH),
        onClick = onClearClicked,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Default.ArrowBack,
          contentDescription = stringResource(
            UiString.clear_search_button_content_description,
          ),
        )
      }
    }
  }
}

@Composable
private fun SearchField(
  modifier: Modifier = Modifier,
  value: String? = null,
  onQueryChanged: (String) -> Unit,
  typeOptions: List<StringResource>,
  typeIndex: Int,
) {
  val searchContentDescription = stringResource(UiString.core_ui_toolbar_search_placeholder)

  BasicTextFieldWithCursorAtEnd(
    modifier = modifier
      .semantics { contentDescription = searchContentDescription },
    value = value ?: "",
    textStyle = TextStyle(
      fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
      fontSize = MaterialTheme.typography.titleMedium.fontSize,
      color = MaterialTheme.colorScheme.onSurface,
    ),
    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions.Default,
    onValueChange = onQueryChanged,
    singleLine = true,
    decorationBox = { innerTextField ->
      if (value.isNullOrEmpty()) {
        SearchPlaceHolder(
          modifier = Modifier.fillMaxWidth(),
          typeOptions = typeOptions,
          typeIndex = typeIndex,
        )
      }
      innerTextField()
    },
  )
}

enum class ToolbarState {
  Focused,
  Unfocused,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Previews
private fun SearchBarPreview() {
  AppTheme {
    Surface {
      ScenePeekSearchBar(
        actions = {},
        onClearClicked = {},
        onFocused = {},
        onSearchFieldChanged = {},
      )
    }
  }
}

@Composable
@Previews
private fun FocusedSearchBarPreview() {
  AppTheme {
    Surface {
      ScenePeekSearchBar(
        state = ToolbarState.Focused,
        actions = {},
        onClearClicked = {},
        onFocused = {},
        onSearchFieldChanged = {},
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Previews
private fun FilledSearchBarPreview() {
  AppTheme {
    Surface {
      ScenePeekSearchBar(
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
        onFocused = {},
        isLoading = true,
      )
    }
  }
}
