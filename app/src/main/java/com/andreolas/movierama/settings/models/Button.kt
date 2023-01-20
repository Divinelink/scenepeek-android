@file:Suppress("MaxLineLength")
package com.andreolas.movierama.settings.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.andreolas.movierama.R
import com.andreolas.movierama.databinding.DslButtonPrimaryBinding
import com.andreolas.movierama.databinding.DslButtonSecondaryBinding
import com.andreolas.movierama.databinding.DslButtonTonalBinding
import com.andreolas.movierama.settings.DSLSettingsIcon
import com.andreolas.movierama.settings.DSLSettingsText
import com.andreolas.movierama.settings.PreferenceModel
import gr.divinelink.core.util.mapping.LayoutFactory
import gr.divinelink.core.util.mapping.MappingAdapter
import gr.divinelink.core.util.mapping.MappingViewHolder
import com.google.android.material.button.MaterialButton

object Button {

    fun register(mappingAdapter: MappingAdapter) {
        mappingAdapter.registerFactory<Model.Primary>(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ViewHolder(DslButtonPrimaryBinding.inflate(inflater, parent, false)) }))
        mappingAdapter.registerFactory<Model.Tonal>(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ViewHolder(DslButtonTonalBinding.inflate(inflater, parent, false)) }))
        mappingAdapter.registerFactory<Model.SecondaryNoOutline>(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ViewHolder(DslButtonSecondaryBinding.inflate(inflater, parent, false)) }))
    }

    sealed class Model<T : Model<T>>(
        title: DSLSettingsText?,
        icon: DSLSettingsIcon?,
        isEnabled: Boolean,
        val onClick: () -> Unit
    ) : PreferenceModel<T>(
        title = title,
        icon = icon,
        isEnabled = isEnabled
    ) {
        class Primary(
            title: DSLSettingsText?,
            icon: DSLSettingsIcon?,
            isEnabled: Boolean,
            onClick: () -> Unit
        ) : Model<Primary>(title, icon, isEnabled, onClick)

        class Tonal(
            title: DSLSettingsText?,
            icon: DSLSettingsIcon?,
            isEnabled: Boolean,
            onClick: () -> Unit
        ) : Model<Tonal>(title, icon, isEnabled, onClick)

        class SecondaryNoOutline(
            title: DSLSettingsText?,
            icon: DSLSettingsIcon?,
            isEnabled: Boolean,
            onClick: () -> Unit
        ) : Model<SecondaryNoOutline>(title, icon, isEnabled, onClick)
    }

    class ViewHolder<T : Model<T>>(binding: ViewBinding) : MappingViewHolder<T>(binding.root) {
        private val button: MaterialButton = itemView.findViewById(R.id.button)

        override fun bind(model: T) {
            button.text = model.title?.resolve(context)
            button.setOnClickListener {
                model.onClick()
            }
            button.icon = model.icon?.resolve(context)
            button.isEnabled = model.isEnabled
        }
    }
}
