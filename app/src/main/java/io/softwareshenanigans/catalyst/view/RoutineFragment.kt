package io.softwareshenanigans.catalyst.view

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.softwareshenanigans.catalyst.R
import io.softwareshenanigans.catalyst.model.LocationService
import io.softwareshenanigans.catalyst.model.routine.Routine
import kotlinx.android.synthetic.main.routine_fragment.*
import java.lang.Exception


class RoutineFragment : Fragment() {
    private val TAG = "RoutineFragment"

    companion object {
        fun newInstance() = RoutineFragment()
    }

    private lateinit var viewModel: RoutineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.routine_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RoutineViewModel::class.java)
        viewModel.allRoutines.observe(viewLifecycleOwner, Observer {
            routine_spinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, it)
        })
        routine_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.selectedRoutine.value = viewModel.allRoutines.value!![position].reference
            }

        }

        routine_step_view.layoutManager = LinearLayoutManager(this.context)

        viewModel.selectedRoutine.observe(viewLifecycleOwner, Observer {
            routine_step_view.adapter = RoutineStepViewAdapter(it)
        })
        startRunButton.setOnClickListener {
            onStartRunClick()
        }
    }

    class RoutineStepViewAdapter(val routine: Routine):
        RecyclerView.Adapter<RoutineStepViewAdapter.ViewHolder>() {
        class ViewHolder(val stepView: View, val stepDescription: TextView, val stepDuration: TextView): RecyclerView.ViewHolder(stepView)

        val hasChildrenViewType = 1
        val noChildrenViewType = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(when (viewType) {
                    hasChildrenViewType -> R.layout.routine_sub_entry
                    noChildrenViewType -> R.layout.routine_time_interval
                    else -> throw Exception()
                }, parent, false)

            return ViewHolder(itemView,
                itemView.findViewById(R.id.stepDescription),
                itemView.findViewById(R.id.stepDuration))
        }

        override fun getItemCount(): Int {
            return routine.steps.count()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.stepDescription.text = routine.steps[position].displayName
            holder.stepDuration.text = DateUtils.formatElapsedTime(routine.steps[position].duration.toLong())
        }

        override fun getItemViewType(position: Int): Int {
            return noChildrenViewType
        }
    }


    private fun onStartRunClick() {
        Log.i(TAG, "onStartRunClick")

        context!!.startService(Intent(context!!, LocationService::class.java))
    }

}

