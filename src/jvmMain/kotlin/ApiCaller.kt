package xyz.nietongxue.soccerTime

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


enum class TaskResult {
    SUCCESS,
    ERROR,
    INIT,
    CANCEL
}

interface Scheduler {
    fun next(preResult: TaskResult): Instant

    companion object {
        val default = object : Scheduler {
            override fun next(preResult: TaskResult): Instant {
                return when (preResult) {
                    TaskResult.SUCCESS -> now().plus(1.hours)
                    TaskResult.INIT -> now().plus(3.seconds)
                    TaskResult.ERROR -> now().plus(1.minutes)
                    else -> error("not supported")
                }
            }
        }
        val longTerm =object : Scheduler {
            override fun next(preResult: TaskResult): Instant {
                return when (preResult) {
                    TaskResult.SUCCESS -> now().plus(1.days)
                    TaskResult.INIT -> now().plus(3.seconds)
                    TaskResult.ERROR -> now().plus(1.minutes)
                    else -> error("not supported")
                }
            }
        }
    }
}

abstract class ApiCaller {
    private var timer: Timer? = null
    abstract val apiId: String
    abstract val scheduler: Scheduler
    abstract val user: ApiUser
    private suspend fun actionImpl() {
        user.getting()
    }

    class T(val caller:ApiCaller) : TimerTask() {
        override fun run() {
            caller. action()
        }
    }

    fun action() {
        runBlocking {
            try {
                actionImpl()
                ServiceStatusRepository.addCalling(Calling(apiId, now().toEpochMilliseconds(), Status.SUCCESS, ""))
            } catch (e: Exception) {
                ServiceStatusRepository.addCalling(Calling(apiId, now().toEpochMilliseconds(), Status.ERROR, e.message))
            }
            val instant = scheduler.next(TaskResult.SUCCESS)
            timer!!.schedule(T(this@ApiCaller), instant.toDate())
        }
    }

    fun init() {
        timer = Timer("timer-$apiId")
        val initInstant = scheduler.next(TaskResult.INIT)
        timer!!.schedule(T(this@ApiCaller), initInstant.toDate())
    }
}

