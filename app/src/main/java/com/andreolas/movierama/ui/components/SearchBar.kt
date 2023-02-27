@file:Suppress("LongMethod")

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import android.provider.Settings.Global.getString
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.getString
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.SearchBarSize

const val SEARCH_BAR_LOADING_INDICATOR_TAG = "SEARCH_BAR_LOADING_INDICATOR"

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String? = null,
    onClearClicked: () -> Unit,
    onSearchFieldChanged: (String) -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    state: ToolbarState = ToolbarState.Unfocused,
    isLoading: Boolean = false,
) {
    val toolbarState = remember { mutableStateOf(state) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(toolbarState.value) {
        if (toolbarState.value == ToolbarState.Focused) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 36.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Crossfade(targetState = toolbarState.value) { toolbar ->
            if (toolbar == ToolbarState.Focused || searchValue?.isNotEmpty() == true) {
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
                        value = searchValue,
                        onSearchFieldChanged = onSearchFieldChanged,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(SearchBarSize)
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
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    )
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        text = stringResource(id = R.string.toolbar_search),
                        style = MaterialTheme.typography.titleMedium,
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
                    .padding(start = 16.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
                    .height(24.dp)
                    .width(24.dp)
            )
        }
        false -> {
            IconButton(
                modifier = Modifier.padding(start = 2.dp),
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
            .padding(end = 12.dp),
        value = value ?: "",
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
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
            SearchBar(
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
            SearchBar(
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
            SearchBar(
                state = ToolbarState.Focused,
                searchValue = "Flight Club",
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
