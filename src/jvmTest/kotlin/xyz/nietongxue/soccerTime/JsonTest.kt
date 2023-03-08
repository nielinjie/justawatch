package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Test
import kotlin.test.assertEquals


@Serializable
data class A(val a: Int, val b: Int)

class JsonTest:StringSpec( {
     val json1 = Json { ignoreUnknownKeys = true }

    "test" {
        val json = """
            {"a":1,"b":2,"c":3}
        """
        val a = json1.decodeFromString(A.serializer(), json)
        assertEquals(a, A(1, 2))
    }

     "testByPath" {
        val json = """
            {"a":{"a1":1}
            ,"b":2,"c":3}
        """
        val je = Json.parseToJsonElement(json)
        val re = je.jsonObject.getByPath("a.a1")
        assertEquals(1, re?.jsonPrimitive?.int)
    }

})