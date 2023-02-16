package xyz.nietongxue.soccerTime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.sergeych.sprintf.sprintf
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class DurationOffset(val after: Boolean, val string: String)

fun toNaturelString(duration: Duration, time: Long): String {
    val ins = Instant.fromEpochMilliseconds(time)
    val string = ins.toLocalDateTime(TimeZone.currentSystemDefault()).let {
        "%02d:%02d".sprintf( it.hour, it.minute)
    }
    return duration.toComponents { days, hours, minutes, _, _ ->
        if (days > 0) "$days days"
        else if (hours > 0) "$hours hours - $string"
        else if (minutes > 0) "$minutes minutes - $string"
        else "now"
    }
}

fun durationOffset(time: Long, zero: Long): DurationOffset {

    return if (time > zero) {
        DurationOffset(true, toNaturelString((time - zero).toDuration(DurationUnit.MILLISECONDS), time))
    } else DurationOffset(false, toNaturelString((zero - time).toDuration(DurationUnit.MILLISECONDS), time))
}
