package xyz.nietongxue.soccerTime.util

import web.timers.setTimeout

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




@JsNonModule
@JsModule("color")
external class ColorU(value:String)
{
    fun lighten(fl: Float): ColorU
    fun string(): String

}