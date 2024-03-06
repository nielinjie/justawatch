package xyz.nietongxue.soccerTime

import io.klogging.logger

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


sealed class TaskResult {
    class SUCCESS<R>(val result: R) : TaskResult()
    class ERROR(val e: Throwable) : TaskResult()
    object INIT : TaskResult()
    object CANCEL : TaskResult()
}

interface Scheduler {
    fun next(preResult: TaskResult): Instant

    companion object {
        val default = object : Scheduler {
            override fun next(preResult: TaskResult): Instant {
                return when (preResult) {
                    is TaskResult.SUCCESS<*> -> now().plus(1.hours)
                    TaskResult.INIT -> now().plus(3.seconds)
                    is TaskResult.ERROR -> now().plus(10.minutes)
                    else -> error("not supported")
                }
            }
        }
        val longTerm = object : Scheduler {
            override fun next(preResult: TaskResult): Instant {
                return when (preResult) {
                    is TaskResult.SUCCESS<*> -> now().plus(1.days)
                    TaskResult.INIT -> now().plus(3.seconds)
                    is TaskResult.ERROR -> now().plus(1.hours)
                    else -> error("not supported")
                }
            }
        }
    }
}

abstract class ApiCaller<R> {
    val logger = logger("api caller")
    private var timer: Timer? = null
    abstract val apiId: String
    abstract val scheduler: Scheduler
    abstract val user: ApiUser<R>
    private suspend fun actionImpl(): R {
        //TODO 这里需要研究，这样子可能是串行的。
        return user.getting()
    }

    class T<TR>(val caller: ApiCaller<TR>) : TimerTask() {
        override fun run() {
            caller.action()
        }
    }
    fun restart() {
        timer?.cancel()
        init()
    }
    fun action() {
        runBlocking {
            try {
                val r = actionImpl()
                ServiceStatusRepository.addCalling(Calling(apiId, now().toEpochMilliseconds(), Status.SUCCESS, ""))
                val instant = scheduler.next(TaskResult.SUCCESS(r))
                timer!!.schedule(T(this@ApiCaller), instant.toDate())
                logger.info("scheduled for success, next calling at $instant")
            } catch (e: Exception) {
                logger.error(e)
                ServiceStatusRepository.addCalling(Calling(apiId, now().toEpochMilliseconds(), Status.ERROR, e.message))
                val instant = scheduler.next(TaskResult.ERROR(e))
                timer!!.schedule(T(this@ApiCaller), instant.toDate())
                logger.info("scheduled for error, next calling at $instant")
            }

        }
    }

    fun init() {
        runBlocking {
            timer = Timer("timer-$apiId")
            val initInstant = scheduler.next(TaskResult.INIT)
            timer!!.schedule(T(this@ApiCaller), initInstant.toDate())
            logger.info("scheduled for init, next calling at $initInstant")
        }
    }
}

