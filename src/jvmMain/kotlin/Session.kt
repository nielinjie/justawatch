package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable
import java.util.spi.AbstractResourceBundleProvider

class SessionRepository {
    val sessions = mutableMapOf<String, Session>()

    fun get(sessionId: String): Session? {
        return sessions[sessionId]
    }

    fun put(session: Session) {
        this.sessions.put(session.id, session)
    }
}

class UserRepository {
    val users = mutableMapOf<UserSession, UserInfo>()

    suspend fun get(userSession: UserSession): UserInfo {
        return users[userSession] ?: run {
            val userInfo = getUserGithub(userSession.token)
            put(userSession, userInfo)
            userInfo
        }
    }

    fun put(userSession: UserSession, userInfo: UserInfo) {
        users.put(userSession, userInfo)
    }
}


