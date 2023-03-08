package xyz.nietongxue.soccerTime

class Tagging(private val session: Session,val app: App,val filtering: TaggerFiltering) {

    private val taggers = session.taggers
    fun tagging(list: List<Fixture>): List<FixtureWithTags> {
        val map = mutableMapOf<Int, MutableList<Tag>>()
        list.forEach {
            map[it.id] = mutableListOf()
        }
        taggers.forEach { it ->
            val taggingList = filtering.filter(list, it)
            val tag = Tag("filter - ${it.description}")
            taggingList.forEach {
                map[it.id]?.add(tag)
            }
        }
        return list.map {
            FixtureWithTags(it, map[it.id] ?: emptyList())
        }
    }
}

