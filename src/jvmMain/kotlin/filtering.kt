package xyz.nietongxue.soccerTime

class Filtering(private val session: Session) {
    fun filter(list: List<FixtureDetailed>): List<FixtureDetailed> {
        val filters = session.customize.filters
        var re = list
        filters.forEach { en ->
            en.value.forEach {
                re = map(en.key to it, re)
            }
        }
        return re
    }

    private fun map(f: Pair<UnderLine, Filter>, list: List<FixtureDetailed>): List<FixtureDetailed> {
        return when (f.second) {
            is NegativeTagsFilter -> {
                addUnderLineOn(
                    list,
                    UnderLine.COLLAPSED
                ) { it.tags.any { tag -> tag.power == Power.NEGATIVE } && !it.tags.any { tag -> tag.power == Power.HIGH } }
            }

            is NoTagFilter -> {
                addUnderLineOn(list, UnderLine.COLLAPSED) { it.tags.isEmpty() }
            }

            else -> TODO()
        }
    }

    private fun addUnderLineOn(
        list: List<FixtureDetailed>, underLine: UnderLine, pre: (fix: FixtureDetailed) -> Boolean,
    ) = list.map {
        if (pre(it)) {
            it.copy(underLines = it.underLines + underLine)
        } else it
    }

}