package gr.divinelink.core.util.timer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

enum class PlayerMode {
    PLAYING,
    PAUSED,
    STOPPED
}

class Timer(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    val millisInFuture: Long,
    val countDownInterval: Long = 1000L,
    runAtStart: Boolean = false,
    val onFinish: (() -> Unit)? = null,
    val onTick: ((Long) -> Unit)? = null
) {
    private var job: Job = Job()
    private val _tick = MutableStateFlow(0L)
    val tick = _tick.asStateFlow()
    private val _playerMode = MutableStateFlow(PlayerMode.STOPPED)
    val playerMode = _playerMode.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        if (runAtStart) start()
    }

    fun start() {
        if (_tick.value == 0L) _tick.value = millisInFuture
        job.cancel()
        job = scope.launch(dispatcher) {
            _playerMode.value = PlayerMode.PLAYING
            while (isActive) {
                if (_tick.value <= 0) {
                    job.cancel()
                    onFinish?.invoke()
                    _playerMode.value = PlayerMode.STOPPED
                    return@launch
                }
                delay(timeMillis = countDownInterval)
                _tick.value -= countDownInterval
                onTick?.invoke(this@Timer._tick.value)
            }
        }
    }

    fun pause() {
        job.cancel()
        _playerMode.value = PlayerMode.PAUSED
    }

    fun stop() {
        job.cancel()
        _tick.value = 0
        _playerMode.value = PlayerMode.STOPPED
    }
}

class PreciseCountdown constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val totalTime: Long,
    private val interval: Long,
    private val delay: Long = 0,
    val onTick: ((Long) -> Unit)? = null,
    val onFinish: (() -> Unit)? = null,
) :
    Timer("PreciseCountdown", true) {
    private var task: TimerTask
    private var startTime: Long = -1
    private var restart = false
    private var wasCancelled = false
    private var wasStarted = false

    private var job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun start() {
        wasStarted = true
        this.scheduleAtFixedRate(task, delay, interval)
    }

    fun restart() {
        if (!wasStarted) {
            start()
        } else if (wasCancelled) {
            wasCancelled = false
            task = getTask(totalTime)
            start()
        } else {
            restart = true
        }
    }

    fun stop() {
        wasCancelled = true
        task.cancel()
    }

    // Call this when there's no further use for this timer
    fun dispose() {
        cancel()
        purge()
    }

    private fun getTask(totalTime: Long): TimerTask {
        return object : TimerTask() {
            override fun run() {
                job = scope.launch(dispatcher) {
                    val timeLeft: Long
                    if (startTime < 0 || restart) {
                        startTime = scheduledExecutionTime()
                        timeLeft = totalTime
                        restart = false
                    } else {
                        timeLeft = totalTime - (scheduledExecutionTime() - startTime)
                        if (timeLeft <= 0) {
                            this@PreciseCountdown.cancel()
                            startTime = -1
                            onFinish?.invoke()
                            return@launch
                        }
                    }
                    onTick?.invoke(timeLeft)
                }

            }
        }
    }

    init {
        task = getTask(totalTime)
    }
}

