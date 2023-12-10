package xyz.nietongxue.soccerTime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.sergeych.sprintf.sprintf
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class DurationOffset(val after: Boolean, val string: String, val duration: Duration)

fun toNaturelString(duration: Duration, time: Long): String {
    val ins = Instant.fromEpochMilliseconds(time)
    val string = ins.toLocalDateTime(TimeZone.currentSystemDefault()).let {
        "%02d:%02d".sprintf(it.hour, it.minute)
    }
    return duration.toComponents { days, hours, minutes, _, _ ->
        if (days > 0) "$days days"
        else if (hours > 0) {
            if (minutes > 0) {
                "$hours h $minutes m - $string"
            } else {
                "$hours hours - $string"
            }
        } else if (minutes > 0) "$minutes minutes - $string"
        else "now"
    }
}

fun durationOffset(time: Long, zero: Long): DurationOffset {
    val duration = if (time > zero)
        (time - zero).toDuration(DurationUnit.MILLISECONDS) else
        (zero - time).toDuration(DurationUnit.MILLISECONDS)
    return if (time > zero) {
        DurationOffset(true, toNaturelString(duration, time), duration)
    } else DurationOffset(false, toNaturelString(duration, time), duration)
}
