package io.softwareshenanigans.catalyst.model.routine

interface RoutineStep {
    val children: List<SubStep>?
        get() = null

    /**
     * Seconds
     */
    val duration: Int

    val displayName: String
}