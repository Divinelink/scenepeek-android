package gr.divinelink.core.util.mapping

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal class MappingDiffCallback<T : MappingModel> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return if (oldItem.javaClass === newItem.javaClass) {
            oldItem.areItemsTheSame(newItem)
        } else false
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return if (oldItem.javaClass === newItem.javaClass) {
            oldItem.areContentsTheSame(newItem)
        } else false
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        return if (oldItem.javaClass === newItem.javaClass) {
            oldItem.getChangePayload(newItem)
        } else null
    }
}
