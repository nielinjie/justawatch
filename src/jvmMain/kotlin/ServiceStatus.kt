package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable


enum class Status {
    SUCCESS,
    ERROR,
}

@Serializable
data class Calling(val api: String, val time: Long, val status: Status, val message: String?)

object ServiceStatusRepository {
    private val callingLogs = mutableListOf<Calling>()
    fun calling(api: String?): List<Calling> {
        return (if (api != null)
            callingLogs.filter { api == it.api }
        else callingLogs).sortedByDescending { it.time }
    }

    fun addCalling(calling: Calling) {
        callingLogs.add(calling)
    }
}