package project.c323.bonusproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import project.c323.bonusproject.databinding.FragmentNoteBinding
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteFragment : Fragment() {
    /**
     * Note fragment
     *
     * Fragment for creating the note.
     */
    val TAG = "NoteFragment"
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    // create view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(TasksViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.time.text != "Pick Time" && binding.date.text != "Pick Date") {
                        viewModel.newDate = binding.date.text.toString()
                        viewModel.newTime = binding.time.text.toString()
                    }
                    viewModel.addTask() // Execute your update logic here
                    findNavController().navigateUp() // Navigate up
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

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}