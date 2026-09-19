package com.divinelink.core.ui.modal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePeekBottomSheet(
  isVisible: Boolean,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden),
  shape: Shape = BottomSheetDefaults.ExpandedShape,
  containerColor: Color = BottomSheetDefaults.ContainerColor,
  dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
  content: @Composable ColumnScope.() -> Unit,
) {
  LaunchedEffect(isVisible) {
    if (isVisible) {
      sheetState.show()
    } else {
      sheetState.hide()
      onDismissRequest()
    }
  }

  if (!sheetState.isVisible && !isVisible) {
    return
  }

  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    modifier = modifier,
    sheetState = sheetState,
    shape = shape,
    containerColor = containerColor,
    dragHandle = dragHandle,
    content = content,
  )
}
