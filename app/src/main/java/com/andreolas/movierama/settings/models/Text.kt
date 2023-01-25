package com.andreolas.movierama.settings.models

import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.andreolas.movierama.databinding.DslTextPreferenceBinding
import com.andreolas.movierama.settings.dsl.DSLSettingsText
import com.andreolas.movierama.settings.dsl.PreferenceModel
import gr.divinelink.core.util.mapping.LayoutFactory
import gr.divinelink.core.util.mapping.MappingAdapter
import gr.divinelink.core.util.mapping.MappingViewHolder

/**
 * A Text without any padding, allowing for exact padding to be handed in at runtime.
 */
data class Text(
    val text: DSLSettingsText,
) {

    companion object {
        fun register(adapter: MappingAdapter) {
            adapter.registerFactory(LayoutFactory({ i: LayoutInflater, r: ViewGroup -> ViewHolder(DslTextPreferenceBinding.inflate(i, r, false)) }))
        }
    }

    class Model(val paddableText: Text) : PreferenceModel<Model>() {
        override fun areItemsTheSame(newItem: Any): Boolean {
            return true
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && newItem.paddableText == paddableText
        }
    }

    class ViewHolder(binding: DslTextPreferenceBinding) : MappingViewHolder<Model>(binding.root) {
        private val text: TextView = binding.title

        override fun bind(model: Model) {
            text.text = model.paddableText.text.resolve(context)

            val clickableSpans = (text.text as? Spanned)?.getSpans(0, text.text.length, ClickableSpan::class.java)
            if (clickableSpans?.isEmpty() == false) {
                text.movementMethod = LinkMovementMethod.getInstance()
            } else {
                text.movementMethod = null
            }
        }
    }
}
