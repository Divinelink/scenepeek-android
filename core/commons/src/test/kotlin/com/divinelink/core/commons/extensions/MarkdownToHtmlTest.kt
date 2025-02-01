package com.divinelink.core.commons.extensions

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class MarkdownToHtmlTest {

  @Test
  fun `test basic replacements`() {
    val input = "This is a test & < > \" ' with special characters."
    val expected = "This is a test &amp; &lt; &gt; &quot; &#39; with special characters."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test newline replacement`() {
    val input = "Line 1\r\nLine 2"
    val expected = "Line 1<br>Line 2"
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test bold replacement`() {
    val input = "This is **bold** text."
    val expected = "This is <b>bold</b> text."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test italic replacement`() {
    val input = "This is *italic* text."
    val expected = "This is <i>italic</i> text."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test italic replacement with underscore`() {
    val input = "This is _italic_ text."
    val expected = "This is <i>italic</i> text."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test code replacement`() {
    val input = "This is `code` text."
    val expected = "This is <code>code</code> text."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test combined markdown syntax`() {
    val input = "This is **bold**, *italic*, and `code` text."
    val expected = "This is <b>bold</b>, <i>italic</i>, and <code>code</code> text."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test nested markdown syntax`() {
    val input = "This is **bold with *italic* inside** and `code`."
    val expected = "This is <b>bold with <i>italic</i> inside</b> and <code>code</code>."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test empty string`() {
    val input = ""
    val expected = ""
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test string with no markdown`() {
    val input = "This is a plain text with no markdown."
    val expected = "This is a plain text with no markdown."
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test string with multiple newlines`() {
    val input = "Line 1\r\nLine 2\r\nLine 3"
    val expected = "Line 1<br>Line 2<br>Line 3"
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test string with multiple special characters`() {
    val input = "& < > \" ' & < > \" '"
    val expected = "&amp; &lt; &gt; &quot; &#39; &amp; &lt; &gt; &quot; &#39;"
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }

  @Test
  fun `test string with mixed content`() {
    val input = "This is **bold** text with `code` and *italic*." +
      "\r\nNew line & special chars: < > \" '"
    val expected = "This is <b>bold</b> text with <code>code</code> and <i>italic</i>." +
      "<br>New line &amp; special chars: &lt; &gt; &quot; &#39;"
    assertThat(input.markdownToHtml()).isEqualTo(expected)
  }
}
