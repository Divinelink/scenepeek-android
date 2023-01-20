package gr.divinelink.core.util.views

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * Disable animations for changes to same item
 */
class NoCrossfadeChangeDefaultAnimator : DefaultItemAnimator() {
    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        if (oldHolder === newHolder) {
            dispatchChangeFinished(oldHolder, true)
        } else {
            dispatchChangeFinished(oldHolder, true)
            dispatchChangeFinished(newHolder, false)
        }
        return false
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: List<Any>): Boolean {
        return true
    }
}