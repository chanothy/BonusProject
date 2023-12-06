package project.c323.bonusproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import project.c323.bonusproject.databinding.FragmentEditTaskBinding
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 * Use the [EditNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditNoteFragment : Fragment() {
    /**
     * Allows for editing of notes. When information is changed, it navigates back to the home screen [tasksFragment]
     */
    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    /**
     * Creates view and prepares the [viewModel]
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        val taskId = EditNoteFragmentArgs.fromBundle(requireArguments()).taskId

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = EditTaskViewModelFactory(taskId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditTaskViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.time.text != "Pick Time" && binding.date.text != "Pick Date") {
                        viewModel.newDate = binding.date.text.toString()
                        viewModel.newTime = binding.time.text.toString()
                    }
                    viewModel.updateTask()
                }
            })



        // navigates back to taskFragment on data change.
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editTaskFragment_to_tasksFragment)
                viewModel.onNavigatedToList()
            }
        })

        val pickTimeBtn = binding.time
        pickTimeBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    if (selectedMinute.toString().length == 1) {
                        pickTimeBtn.text = "$selectedHour:0$selectedMinute"

                    }
                    else {
                        pickTimeBtn.text = "$selectedHour:$selectedMinute"
                    }

                },
                hour,
                minute,
                true // 24-hour time format
            )

            timePickerDialog.show()
        }

        val pickDateBtn = binding.date
        pickDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                pickDateBtn.text = "${selectedMonth+1}/$selectedDayOfMonth/$selectedYear"
            }, year, month, dayOfMonth)

            datePickerDialog.show()
        }

        viewModel.task.observe(viewLifecycleOwner) { task ->
            pickDateBtn.text = if (task.date.isNullOrEmpty()) {
                "Pick Date"
            } else {
                task.date
            }
        }

        viewModel.task.observe(viewLifecycleOwner) { task ->
            pickTimeBtn.text = if (task.time.isNullOrEmpty()) {
                "Pick Time"
            } else {
                task.time
            }
        }

        return view
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}