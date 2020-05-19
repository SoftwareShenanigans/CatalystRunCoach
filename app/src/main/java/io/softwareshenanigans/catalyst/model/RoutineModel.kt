package io.softwareshenanigans.catalyst.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.softwareshenanigans.catalyst.model.routine.RepeatRoutineStep
import io.softwareshenanigans.catalyst.model.routine.Routine
import io.softwareshenanigans.catalyst.model.routine.RoutineStepType
import io.softwareshenanigans.catalyst.model.routine.TimeIntervalRoutineStep

object RoutineModel {
    val selectedRoutine = MutableLiveData<Routine>()
    private val _allRoutines = MutableLiveData<List<Routine>>()
    val allRoutines: LiveData<List<Routine>>
        get() = _allRoutines

    init {
        _allRoutines.value = listOf(Routine("C25k week 1", listOf(
            TimeIntervalRoutineStep(5*60, RoutineStepType.WARM_UP),
            RepeatRoutineStep(8, listOf(
                TimeIntervalRoutineStep(60, RoutineStepType.RUN),
                TimeIntervalRoutineStep(90, RoutineStepType.REST)
            )),
            TimeIntervalRoutineStep(5*60, RoutineStepType.COOL_DOWN)
        )))
    }
}