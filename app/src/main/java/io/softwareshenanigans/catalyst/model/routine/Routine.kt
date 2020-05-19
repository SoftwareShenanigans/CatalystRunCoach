package io.softwareshenanigans.catalyst.model.routine

data class Routine(val name: String, val steps: List<RoutineStep>) {
    val duration: Int = steps.sumBy { it.duration }

    /**
     * Assuming offsetSeconds = 0 is the beginning of the routine, finds the RoutineStep that
     * would be current for the given offset in seconds.
     *
     * If offsetSeconds > the total length of the routine, returns null.
     */
    fun stepFor(offsetSeconds: Int): RoutineStep? {
        var remaining = offsetSeconds
        var stepIndex = 0
        do {
            remaining -= steps[stepIndex++].duration
        } while (remaining > 0)

        if (remaining > 0) return null

        return steps[stepIndex]
    }
}