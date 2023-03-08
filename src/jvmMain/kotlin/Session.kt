package xyz.nietongxue.soccerTime

class SessionRepository {
    private val sessions = mutableMapOf<String, Session>()



    fun get(sessionId: String): Session? {
        return sessions[sessionId]
    }
}

