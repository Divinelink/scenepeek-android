package gr.divinelink.core.util.mapping

import android.view.ViewGroup

interface Factory<T : MappingModel> {
    fun createViewHolder(parent: ViewGroup): MappingViewHolder<T>
}
