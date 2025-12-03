package com.divinelink.core.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

actual fun String.fromHtml(): AnnotatedString = buildAnnotatedString {
  var currentIndex = 0
  val tagStack = mutableListOf<String>()
  val styleStack = mutableListOf<Int>()

  val tagRegex = Regex("<(/?)([a-zA-Z0-9]+)[^>]*>")
  val matches = tagRegex.findAll(this@fromHtml)

  matches.forEach { match ->
    val textBefore = this@fromHtml.substring(currentIndex, match.range.first)
    append(textBefore)

    val isClosing = match.groupValues[1] == "/"
    val tagName = match.groupValues[2].lowercase()

    if (!isClosing) {
      val startIndex = length
      styleStack.add(startIndex)
      tagStack.add(tagName)
    } else {
      if (tagStack.isNotEmpty() && tagStack.last() == tagName) {
        val startIndex = styleStack.removeLastOrNull() ?: 0
        val style = when (tagStack.removeLastOrNull()) {
          "b", "strong" -> SpanStyle(fontWeight = FontWeight.ExtraBold)
          "i", "em" -> SpanStyle(fontStyle = FontStyle.Italic)
          "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
          else -> null
        }
        style?.let {
          addStyle(it, startIndex, length)
        }
      }
    }

    currentIndex = match.range.last + 1
  }

  if (currentIndex < this@fromHtml.length) {
    append(this@fromHtml.substring(currentIndex))
  }
}
