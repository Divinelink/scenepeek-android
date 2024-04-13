package gr.divinelink.core.util.extensions

import android.content.Context
import android.graphics.Typeface
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.AlignmentSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import gr.divinelink.core.util.R

fun CharSequence?.bold(): CharSequence {
  val spannable = SpannableString(this)
  spannable.setSpan(
    StyleSpan(Typeface.BOLD),
    0,
    this?.length ?: 0,
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
  )
  return spannable
}

fun CharSequence?.textAppearance(context: Context, @StyleRes textAppearance: Int): CharSequence {
  val spannable = SpannableString(this)
  spannable.setSpan(
    TextAppearanceSpan(context, textAppearance),
    0,
    this?.length ?: 0,
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
  )
  return spannable
}

fun CharSequence?.center(): CharSequence {
  val spannable = SpannableString(this)
  spannable.setSpan(
    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
    0,
    this?.length ?: 0,
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
  )
  return spannable
}

fun CharSequence?.color(color: Int): CharSequence {
  val spannable = SpannableString(this)
  spannable.setSpan(
    ForegroundColorSpan(color),
    0,
    this?.length ?: 0,
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
  )
  return spannable
}

fun CharSequence?.learnMore(
  context: Context,
  @ColorInt color: Int,
  onLearnMoreClicked: View.OnClickListener
): CharSequence {
  val learnMore = context.getString(R.string.LearnMoreTextView_learn_more)
  return clickSubstring(learnMore, learnMore, onLearnMoreClicked, color)
}

private fun clickSubstring(
  fullString: CharSequence,
  substring: CharSequence,
  clickListener: View.OnClickListener,
  @ColorInt linkColor: Int
)
  : CharSequence {
  val clickable: ClickableSpan = object : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
      super.updateDrawState(ds)
      ds.isUnderlineText = false
      ds.color = linkColor
    }

    override fun onClick(widget: View) {
      clickListener.onClick(widget)
    }
  }
  val spannable = SpannableString(fullString)
  val start = TextUtils.indexOf(fullString, substring)
  val end = start + substring.length
  if (start >= 0 && end <= fullString.length) {
    spannable.setSpan(clickable, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  }
  return spannable
}
