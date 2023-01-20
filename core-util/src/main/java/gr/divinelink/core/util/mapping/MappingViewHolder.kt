package gr.divinelink.core.util.mapping

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

abstract class MappingViewHolder<Model>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val context: Context
    protected val payload: MutableList<Any>
    fun <T : View?> findViewById(@IdRes id: Int): T {
        return itemView.findViewById(id)
    }

    fun onAttachedToWindow() {
        // Intentionally Blank.
    }

    fun onDetachedFromWindow() {
        // Intentionally Blank.
    }

    abstract fun bind(model: Model)

    fun setPayload(payload: List<Any>) {
        this.payload.clear()
        this.payload.addAll(payload)
    }

    open class SimpleViewHolder<Model>(itemView: View) : MappingViewHolder<Model>(itemView) {
        override fun bind(model: Model) {
            // Intentionally Blank.
        }
    }

    init {
        context = itemView.context
        payload = LinkedList()
    }
}
