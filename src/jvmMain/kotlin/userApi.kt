package xyz.nietongxue.soccerTime

import io.klogging.logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getUserGithub(token:String):UserInfo {
    fun withResponse(string: String):UserInfo{
        val json = Json.parseToJsonElement(string)
        val id = json.jsonObject.getByPath("id")?.jsonPrimitive?.content ?: error("id not found")
        val name = json.jsonObject.getByPath("name")?.jsonPrimitive?.content ?: error("name not found")
        return UserInfo("github",id,name)
    }
    val logger = logger("api user")

    val client = HttpClient(CIO)
    /*
    curl -L \
      -H "Accept: application/vnd.github+json" \
      -H "Authorization: Bearer <YOUR-TOKEN>" \
      -H "X-GitHub-Api-Version: 2022-11-28" \
      https://api.github.com/user

     */
    client.get("https://api.github.com/user"){
        headers {
            append(HttpHeaders.Accept,"application/vnd.github+json")
            append(HttpHeaders.Authorization,"Bearer $token")
        }
    }.also {response  ->
        if (response.status == HttpStatusCode.OK) {
            val stringBody: String = response.body()
            logger.info("calling api succeeded - github/user")
            val r = withResponse(stringBody)
            logger.info("proceeded - github/user")
            return r
        } else {
            logger.error("calling api failed - github/user")
            logger.info(response.body<String>())
            error("response error - ${response.status}")
        }

    }
}