package gr.divinelink.core.util.mapping

interface MappingModel {
    fun areItemsTheSame(newItem: Any): Boolean
    fun areContentsTheSame(newItem: Any): Boolean
    fun getChangePayload(newItem: Any): Any? {
        return null
    }
}

/**
 * A [MappingModel] used only when you don't want DiffUtil comparisons.
 */

interface EmptyMappingModel : MappingModel {
    override fun areItemsTheSame(newItem: Any): Boolean = true
    override fun areContentsTheSame(newItem: Any): Boolean = true
    override fun getChangePayload(newItem: Any): Any? = null
}
