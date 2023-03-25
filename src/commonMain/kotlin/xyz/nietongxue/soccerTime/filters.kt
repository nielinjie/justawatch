package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

enum class CompositeOperator {
    AND,
    OR
}


interface Filter {
    val description: String
}

@Serializable

object NegativeTagsFilter: Filter {
    override val description: String
        = "Collapse any NEGATIVE tag"
}
object NoTagFilter: Filter {
    override val description: String = "Collapse any without tag"
}

@Serializable
data class Composite(val filters: List<Filter>, val operator: CompositeOperator) : Filter{
    override val description: String
        get() = TODO("Not yet implemented")

}