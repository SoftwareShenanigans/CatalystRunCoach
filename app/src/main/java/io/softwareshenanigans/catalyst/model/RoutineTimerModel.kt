package io.softwareshenanigans.catalyst.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.softwareshenanigans.catalyst.model.routine.Routine
import io.softwareshenanigans.catalyst.model.routine.RoutineStep
import java.util.*
import java.util.concurrent.TimeUnit

object RoutineTimerModel {
    private val timer = Timer()

    private val _timerState = MutableLiveData<WorkoutState>()
    val timerState: LiveData<WorkoutState>
        get() = _timerState

    /**
     * start the selectedRoutine (from RoutineModel) from the beginning.
     */
    fun run(routine: Routine) {
        _timerState.value = WorkoutState.Running(routine, timer.getWallClockTime())
        update()
    }

    private fun update() {
        val state = _timerState.value
        if (state !is WorkoutState.Running) return

        val elapsedSec =
            TimeUnit.SECONDS.convert(timer.getIntervalMsecSince(state.startNanos), TimeUnit.MILLISECONDS)
                .toInt()

        val currentStep = state.routine.stepFor(elapsedSec)

        if (currentStep == null) {
            _timerState.value = WorkoutState.Stopped()
            return
        }

        _timerState.value = WorkoutState.Running(state.routine, state.startNanos, elapsedSec, currentStep, state.startTime)

        timer.callMeAfter(1, Runnable { update() })
    }

    /**
     * stops the timer.
     */
    fun stop() {
        timer.dropAllPendingTasks()
        _timerState.value = WorkoutState.Stopped()
    }
}

sealed class WorkoutState {
    //TODO: assumes no 0-step routines exist
    class Running(val routine: Routine,
                  val startNanos: Long,
                  val elapsedSec: Int = 0,
                  val currentStep: RoutineStep = routine.steps.first(),
                  val startTime: Date = Date()) : WorkoutState()
    class Stopped(): WorkoutState()
}
