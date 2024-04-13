package com.andreolas.movierama.ui.views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.andreolas.movierama.R
import gr.divinelink.core.util.utils.CommunicationActions
import gr.divinelink.core.util.R as coreR

class LearnMoreTextView : AppCompatTextView {
  private var linkListener: OnClickListener? = null
  private lateinit var link: Spannable
  private var visible = false
  private var baseText: CharSequence? = null
  private var linkColor = 0

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init()
  }

  private fun init() {
    movementMethod = LinkMovementMethod.getInstance()
    setLinkTextInternal(coreR.string.LearnMoreTextView_learn_more)
    setLinkColor(ContextCompat.getColor(context, R.color.colorOnSurface))
    visible = true
  }

  override fun setText(text: CharSequence, type: BufferType) {
    baseText = text
    setTextInternal(baseText, type)
  }

  override fun setTextColor(color: Int) {
    super.setTextColor(color)
  }

  fun setOnLinkClickListener(listener: OnClickListener?) {
    linkListener = listener
  }

  fun setLearnMoreVisible(visible: Boolean) {
    this.visible = visible
    setTextInternal(baseText, if (visible) BufferType.SPANNABLE else BufferType.NORMAL)
  }

  fun setLearnMoreVisible(visible: Boolean, @StringRes linkText: Int) {
    setLinkTextInternal(linkText)
    this.visible = visible
    setTextInternal(baseText, if (visible) BufferType.SPANNABLE else BufferType.NORMAL)
  }

  fun setLink(url: String) {
    setOnLinkClickListener(OpenUrlOnClickListener(url))
  }

  fun setLinkColor(@ColorInt linkColor: Int) {
    this.linkColor = linkColor
  }

  private fun setLinkTextInternal(@StringRes linkText: Int) {
    val clickable: ClickableSpan = object : ClickableSpan() {
      override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = linkColor
      }

      override fun onClick(widget: View) {
        if (linkListener != null) {
          linkListener!!.onClick(widget)
        }
      }
    }
    link = SpannableString(context.getString(linkText))
    link.setSpan(clickable, 0, link.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
  }

  private fun setTextInternal(text: CharSequence?, type: BufferType) {
    if (visible) {
      val builder = SpannableStringBuilder()
      builder.append(text).append(' ').append(link)
      super.setText(builder, BufferType.SPANNABLE)
    } else {
      super.setText(text, type)
    }
  }

  private class OpenUrlOnClickListener(private val url: String) : OnClickListener {
    override fun onClick(v: View) {
      CommunicationActions.openBrowserLink(v.context, url)
    }
  }
}
