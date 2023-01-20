package gr.divinelink.core.util.mapping

import android.view.LayoutInflater
import android.view.ViewGroup

class LayoutFactory<T : MappingModel>(
    private val viewHolder: (layoutInflater: LayoutInflater, parent: ViewGroup) -> MappingViewHolder<T>,
    private val layoutInflater: (parent: ViewGroup) -> LayoutInflater = { parent -> LayoutInflater.from(parent.context) }
) : Factory<T> {
    override fun createViewHolder(parent: ViewGroup): MappingViewHolder<T> {
        return viewHolder(layoutInflater(parent), parent)
    }
}
