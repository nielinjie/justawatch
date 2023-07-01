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
object NegativeTagsFilter : Filter {
    override val description: String = "Collapse any NEGATIVE tag"
}

@Serializable
object NoTagFilter : Filter {
    override val description: String = "Collapse any without tag"
}

@Serializable
data class LeagueFilter(val leagueSessions: List<LeagueSeason>) : Filter {
    override val description: String
        get() = "League in ${leagueSessions.joinToString(",")}"
}

@Serializable
data class RoundsFilter(val rounds: List<String>, val leagueSeason: LeagueSeason) : Filter {
    override val description: String
        get() = "Round ${rounds.joinToString(",")}} in $leagueSeason"
}


@Serializable
data class Composite(val filters: List<Filter>, val operator: CompositeOperator) : Filter {
    override val description: String
        get() = "Composite ${operator.name} ${filters.joinToString(",") { it.description }}"
}