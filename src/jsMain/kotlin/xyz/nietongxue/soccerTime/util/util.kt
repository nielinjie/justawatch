package xyz.nietongxue.soccerTime.util

import kotlinx.browser.document
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.asList
import kotlin.math.abs

fun throttle(callbackFn: () -> Unit, limit: Int): () -> Unit {
    var wait = false;
    return { ->
        if (!wait) {
            callbackFn()
            wait = true;
            setTimeout({
                wait = false;
            }, limit)
        }
    }
}

fun findElementAt(className: String): HTMLLIElement {
    val line = document.getElementsByClassName(className).asList().first()
    val at = line.getBoundingClientRect().top
    val items = document.getElementsByClassName("fixture-item").asList()
    return items.minBy { abs(it.getBoundingClientRect().top - at) } as HTMLLIElement
}