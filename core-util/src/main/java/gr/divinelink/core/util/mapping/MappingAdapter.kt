package gr.divinelink.core.util.mapping

import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.divinelink.core.util.views.NoCrossfadeChangeDefaultAnimator

/**
 * A reusable and composable [RecyclerView.Adapter] built on-top of [ListAdapter] to
 * provide async item diffing support.
 *
 *
 * The adapter makes use of mapping a model class to view holder factory at runtime via one of the [.registerFactory]
 * methods. The factory creates a view holder specifically designed to handle the paired model type. This allows the view holder concretely
 * deal with the model type it cares about. Due to the enforcement of matching generics during factory registration we can safely ignore or
 * override compiler typing recommendations when binding and diffing.
 *
 *
 * General pattern for implementation:
 *
 *  1. Create [MappingModel]s for the items in the list. These encapsulate data massaging methods for views to use and the diff logic.
 *  1. Create [MappingViewHolder]s for each item type in the list and their corresponding [Factory].
 *  1. Create an instance or subclass of [MappingAdapter] and register the mapping of model type to view holder factory for that model type.
 *
 * Event listeners, click or otherwise, are handled at the view holder level and should be passed into the appropriate view holder factories. This
 * pattern mimics how we pass data into view models via factories.
 *
 *
 * NOTE: There can only be on factory registered per model type. Registering two for the same type will result in the last one being used. However, the
 * same factory can be registered multiple times for multiple model types (if the model type class hierarchy supports it).
 */

abstract class MappingAdapter :
    ListAdapter<MappingModel, MappingViewHolder<MappingModel>>(MappingDiffCallback()),
    RegisterFactory {
    var factories: MutableMap<Int, Factory<*>> = HashMap()
    var itemTypes: MutableMap<Any, Int> = HashMap()
    var typeCount: Int = 0

    init {
        this.register()
    }

    override fun onViewAttachedToWindow(holder: MappingViewHolder<MappingModel>) {
        super.onViewAttachedToWindow(holder)
        holder.onAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: MappingViewHolder<MappingModel>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachedFromWindow()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.itemAnimator != null && recyclerView.itemAnimator?.javaClass == DefaultItemAnimator::class.java) {
            recyclerView.itemAnimator = NoCrossfadeChangeDefaultAnimator()
        }
    }

    // Needed to call from Java code
    fun <T : MappingModel> registerFactory(clazz: Class<T>, factory: Factory<T>) {
        val type = typeCount++
        factories[type] = factory
        itemTypes[clazz] = type
    }

    inline fun <reified T : MappingModel> registerFactory(factory: Factory<T>) {
        val type = typeCount++
        factories[type] = factory
        itemTypes[T::class.java] = type
    }

    override fun getItemViewType(position: Int): Int {
        val type = itemTypes[getItem(position).javaClass]
        if (type != null) {
            return type
        }
        throw AssertionError("No view holder factory for type: " + getItem(position)?.javaClass)
    }

    @Suppress("unchecked_cast")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MappingViewHolder<MappingModel> {
        return (factories[viewType]?.createViewHolder(parent) as MappingViewHolder<MappingModel>)
    }

    override fun onBindViewHolder(holder: MappingViewHolder<MappingModel>, position: Int, payloads: List<Any>) {
        holder.setPayload(payloads)
        onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: MappingViewHolder<MappingModel>, position: Int) {
        holder.bind(getItem(position))
    }
}

private interface RegisterFactory {
    fun register()
}
