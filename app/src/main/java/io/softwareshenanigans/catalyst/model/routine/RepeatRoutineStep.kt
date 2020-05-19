package io.softwareshenanigans.catalyst.model.routine

data class RepeatRoutineStep(val iterations: Int, override val children: List<SubStep>):
    RoutineStep {
    override val duration: Int
        get() = children.sumBy { it.duration } * iterations
    override val displayName: String
        get() = "Repeat $iterations times"
}