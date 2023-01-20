package com.andreolas.movierama.settings.models

import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreolas.movierama.databinding.DslAsyncSwitchPreferenceItemBinding
import com.andreolas.movierama.settings.DSLSettingsText
import com.andreolas.movierama.settings.PreferenceModel
import com.andreolas.movierama.settings.PreferenceViewHolder
import gr.divinelink.core.util.mapping.LayoutFactory
import gr.divinelink.core.util.mapping.MappingAdapter

object AsyncSwitch {

    fun register(adapter: MappingAdapter) {
        adapter.registerFactory(LayoutFactory({ i: LayoutInflater, r: ViewGroup -> ViewHolder(DslAsyncSwitchPreferenceItemBinding.inflate(i, r, false)) }))
    }

    class Model(
        override val title: DSLSettingsText,
        override val isEnabled: Boolean,
        val isChecked: Boolean,
        val isProcessing: Boolean,
        val onClick: () -> Unit
    ) : PreferenceModel<Model>() {
        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && isChecked == newItem.isChecked && isProcessing == newItem.isProcessing
        }
    }

    class ViewHolder(binding: DslAsyncSwitchPreferenceItemBinding) : PreferenceViewHolder<Model>(binding) {
        private val switchWidget = binding.switchWidget
        private val switcher = binding.switcher

        override fun bind(model: Model) {
            super.bind(model)
            switchWidget.isEnabled = model.isEnabled
            switchWidget.isChecked = model.isChecked
            binding.root.isEnabled = !model.isProcessing && model.isEnabled
            switcher.displayedChild = if (model.isProcessing) 1 else 0

            binding.root.setOnClickListener {
                if (!model.isProcessing) {
                    binding.root.isEnabled = false
                    switcher.displayedChild = 1
                    model.onClick()
                }
            }
        }
    }
}
