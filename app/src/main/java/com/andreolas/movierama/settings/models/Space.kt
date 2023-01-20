package com.andreolas.movierama.settings.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.updateLayoutParams
import com.andreolas.movierama.databinding.DslSpacePreferenceBinding
import com.andreolas.movierama.settings.PreferenceModel
import gr.divinelink.core.util.mapping.LayoutFactory
import gr.divinelink.core.util.mapping.MappingAdapter
import gr.divinelink.core.util.mapping.MappingViewHolder

/**
 * Adds extra space between elements in a DSL fragment
 */
data class Space(
    @Px val pixels: Int
) {

    companion object {
        fun register(mappingAdapter: MappingAdapter) {
            mappingAdapter.registerFactory(LayoutFactory({ i: LayoutInflater, r: ViewGroup -> ViewHolder(DslSpacePreferenceBinding.inflate(i, r, false)) }))
        }
    }

    class Model(val space: Space) : PreferenceModel<Model>() {
        override fun areItemsTheSame(newItem: Any): Boolean {
            return true
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && newItem.space == space
        }
    }

    class ViewHolder(binding: DslSpacePreferenceBinding) : MappingViewHolder<Model>(binding.root) {
        override fun bind(model: Model) {
            itemView.updateLayoutParams {
                height = model.space.pixels
            }
        }
    }
}
