package io.softwareshenanigans.catalyst.model.routine

data class TimeIntervalRoutineStep(override val duration: Int, val type: RoutineStepType): RoutineStep,
    SubStep {
    override val displayName: String
        get() = "$type"
}