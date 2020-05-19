package io.softwareshenanigans.catalyst.view

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.softwareshenanigans.catalyst.model.RoutineModel
import io.softwareshenanigans.catalyst.model.routine.Routine

class RoutineViewModel : ViewModel() {
    private val model = RoutineModel

    val allRoutines: LiveData<List<DisplayableRoutine>> = Transformations.map(model.allRoutines) { list ->
        list.map {
            DisplayableRoutine(
                it.name,
                DateUtils.formatElapsedTime(it.duration.toLong()),
                it
            )
        }
    }

    val selectedRoutine = MutableLiveData<Routine>()
}

class DisplayableRoutine(val name: String, val duration: String, val reference: Routine) {
    override fun toString(): String {
        return "$name ($duration)"
    }
}
