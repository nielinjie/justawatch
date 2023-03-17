package xyz.nietongxue.soccerTime

class SessionRepository {
     val sessions = mutableMapOf<String, Session>()

    fun get(sessionId: String): Session? {
        return sessions[sessionId]
    }
    fun put(session:Session){
        this.sessions.put(session.id, session)
    }
}

