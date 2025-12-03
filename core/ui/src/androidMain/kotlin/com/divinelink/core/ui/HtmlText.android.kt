package com.divinelink.core.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml

actual fun String.fromHtml(): AnnotatedString = AnnotatedString.fromHtml(this)
