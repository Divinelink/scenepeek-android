@file:Suppress("LongParameterList", "MaxLineLength", "MagicNumbers")

package com.andreolas.movierama.settings

import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.andreolas.movierama.R
import com.andreolas.movierama.databinding.DslDividerItemBinding
import com.andreolas.movierama.databinding.DslLearnMorePreferenceItemBinding
import com.andreolas.movierama.databinding.DslPreferenceItemBinding
import com.andreolas.movierama.databinding.DslRadioPreferenceItemBinding
import com.andreolas.movierama.databinding.DslSectionHeaderBinding
import com.andreolas.movierama.databinding.DslSwitchPreferenceItemBinding
import com.andreolas.movierama.settings.models.AsyncSwitch
import com.andreolas.movierama.settings.models.Button
import com.andreolas.movierama.settings.models.Space
import com.andreolas.movierama.settings.models.Text
import com.andreolas.movierama.ui.views.LearnMoreTextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import gr.divinelink.core.util.extensions.visible
import gr.divinelink.core.util.mapping.LayoutFactory
import gr.divinelink.core.util.mapping.MappingAdapter
import gr.divinelink.core.util.mapping.MappingViewHolder
import gr.divinelink.core.util.swipe.utils.isLtr
import gr.divinelink.core.util.utils.CommunicationActions
import gr.divinelink.core.util.utils.DimensionUnit
import timber.log.Timber

class DSLSettingsAdapter : MappingAdapter() {

    override fun register() {
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ClickPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> LongClickPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> TextPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> LearnMoreTextPreferenceViewHolder(DslLearnMorePreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> RadioListPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> MultiSelectListPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ExternalLinkPreferenceViewHolder(DslPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> DividerPreferenceViewHolder(DslDividerItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> SectionHeaderPreferenceViewHolder(DslSectionHeaderBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> SwitchPreferenceViewHolder(DslSwitchPreferenceItemBinding.inflate(inflater, parent, false)) }))
        registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> RadioPreferenceViewHolder(DslRadioPreferenceItemBinding.inflate(inflater, parent, false)) }))
        Text.register(this)
        Space.register(this)
        Button.register(this)
        AsyncSwitch.register(this)
    }
}

abstract class PreferenceViewHolder<T : PreferenceModel<T>>(val binding: ViewBinding) : MappingViewHolder<T>(binding.root) {
    protected val iconView: ImageView = binding.root.findViewById(R.id.icon)
    private val iconEndView: ImageView? = binding.root.findViewById(R.id.icon_end)
    protected val titleView: TextView = binding.root.findViewById(R.id.title)
    protected val summaryView: TextView = binding.root.findViewById(R.id.summary)

    @CallSuper
    override fun bind(model: T) {
        listOf(itemView, titleView, summaryView).forEach {
            it.isEnabled = model.isEnabled
        }
        val icon = model.icon?.resolve(context)
        iconView.setImageDrawable(icon)
        iconView.visible = icon != null

        val iconEnd = model.iconEnd?.resolve(context)
        iconEndView?.setImageDrawable(iconEnd)
        iconEndView?.visible = iconEnd != null

        val title = model.title?.resolve(context)
        if (title != null) {
            titleView.text = model.title?.resolve(context)
            titleView.visibility = View.VISIBLE
        } else {
            titleView.visibility = View.GONE
        }

        val summary = model.summary?.resolve(context)
        if (summary != null) {
            summaryView.text = summary
            summaryView.visibility = View.VISIBLE

            val spans = (summaryView.text as? Spanned)?.getSpans(0, summaryView.text.length, ClickableSpan::class.java)
            if (spans?.isEmpty() == false) {
                summaryView.movementMethod = LinkMovementMethod.getInstance()
            } else {
                summaryView.movementMethod = null
            }
        } else {
            summaryView.visibility = View.GONE
            summaryView.movementMethod = null
        }
    }
}

class TextPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<TextPreference>(binding)

class LearnMoreTextPreferenceViewHolder(binding: DslLearnMorePreferenceItemBinding) : PreferenceViewHolder<LearnMoreTextPreference>(binding) {
    override fun bind(model: LearnMoreTextPreference) {
        super.bind(model)
        (titleView as LearnMoreTextView).setOnLinkClickListener { model.onClick() }
        (summaryView as LearnMoreTextView).setOnLinkClickListener { model.onClick() }
    }
}

class ClickPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<ClickPreference>(binding) {
    override fun bind(model: ClickPreference) {
        super.bind(model)
        binding.root.setOnClickListener { model.onClick() }
        binding.root.setOnLongClickListener { model.onLongClick?.invoke() ?: false }
    }
}

class LongClickPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<LongClickPreference>(binding) {
    override fun bind(model: LongClickPreference) {
        super.bind(model)
        itemView.setOnLongClickListener {
            model.onLongClick()
            true
        }
    }
}

class RadioListPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<RadioListPreference>(binding) {
    override fun bind(model: RadioListPreference) {
        super.bind(model)

        if (model.selected >= 0) {
            summaryView.visibility = View.VISIBLE
            summaryView.text = model.listItems[model.selected]
        } else {
            summaryView.visibility = View.GONE
            Timber.w("Detected a radio list without a default selection: ${model.dialogTitle}")
        }

        itemView.setOnClickListener {
            var selection = -1
            val builder = MaterialAlertDialogBuilder(context)
                .setTitle(model.dialogTitle.resolve(context))
                .setSingleChoiceItems(model.listItems, model.selected) { dialog, which ->
                    if (model.confirmAction) {
                        selection = which
                    } else {
                        model.onSelected(which)
                        dialog.dismiss()
                    }
                }

            if (model.confirmAction) {
                builder
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        model.onSelected(selection)
                        dialog.dismiss()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                builder.show()
            }
        }
    }
}

class MultiSelectListPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<MultiSelectListPreference>(binding) {
    override fun bind(model: MultiSelectListPreference) {
        super.bind(model)

        summaryView.visibility = View.VISIBLE
        val summaryText = model.selected
            .mapIndexed { index, isChecked -> if (isChecked) model.listItems[index] else null }
            .filterNotNull()
            .joinToString(", ")

        if (summaryText.isEmpty()) {
            summaryView.setText(R.string.preferences__none)
        } else {
            summaryView.text = summaryText
        }

        val selected = model.selected.copyOf()

        itemView.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(model.title.resolve(context))
                .setMultiChoiceItems(model.listItems, selected) { _, _, _ ->
                    // Intentionally empty
                }
                .setNegativeButton(android.R.string.cancel) { d, _ -> d.dismiss() }
                .setPositiveButton(android.R.string.ok) { d, _ ->
                    model.onSelected(selected)
                    d.dismiss()
                }
                .show()
        }
    }
}

class SwitchPreferenceViewHolder(binding: DslSwitchPreferenceItemBinding) : PreferenceViewHolder<SwitchPreference>(binding) {
    private val switchWidget: SwitchMaterial = binding.switchWidget
    override fun bind(model: SwitchPreference) {
        super.bind(model)
        switchWidget.isEnabled = model.isEnabled
        switchWidget.isChecked = model.isChecked
        itemView.setOnClickListener {
            model.onClick()
        }
    }
}

class RadioPreferenceViewHolder(binding: DslRadioPreferenceItemBinding) : PreferenceViewHolder<RadioPreference>(binding) {
    private val radioButton: RadioButton = binding.radioWidget
    override fun bind(model: RadioPreference) {
        super.bind(model)
        radioButton.isChecked = model.isChecked
        itemView.setOnClickListener {
            model.onClick()
        }
    }
}

@Suppress("MagicNumber")
class ExternalLinkPreferenceViewHolder(binding: DslPreferenceItemBinding) : PreferenceViewHolder<ExternalLinkPreference>(binding) {
    override fun bind(model: ExternalLinkPreference) {
        super.bind(model)

        val externalLinkIcon = requireNotNull(ContextCompat.getDrawable(context, R.drawable.ic_open_20))
        externalLinkIcon.setBounds(
            0,
            0,
            DimensionUnit.DP.toPixels(20F).toInt(),
            DimensionUnit.DP.toPixels(20F).toInt(),
        )

        if (isLtr()) {
            titleView.setCompoundDrawables(null, null, externalLinkIcon, null)
        } else {
            titleView.setCompoundDrawables(externalLinkIcon, null, null, null)
        }

        itemView.setOnClickListener { CommunicationActions.openBrowserLink(itemView.context, itemView.context.getString(model.linkId)) }
    }
}

class DividerPreferenceViewHolder(val binding: DslDividerItemBinding) : MappingViewHolder<DividerPreference>(binding.root) {
    override fun bind(model: DividerPreference) = Unit
}

class SectionHeaderPreferenceViewHolder(val binding: DslSectionHeaderBinding) : MappingViewHolder<SectionHeaderPreference>(binding.root) {
    override fun bind(model: SectionHeaderPreference) {
        binding.sectionHeader.text = model.title.resolve(context)
    }
}
