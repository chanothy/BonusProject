package project.c323.bonusproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import project.c323.bonusproject.databinding.FragmentNoteBinding
import project.c323.bonusproject.databinding.FragmentTasksBinding

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

        // when button clicked, it adds the new task and moves back to the home screen
//        var saveButton = binding.saveButton
//        saveButton.setOnClickListener {
//            viewModel.addTask()
//            val action = NoteFragmentDirections.actionNoteFragmentToTasksFragment()
//            this.findNavController().navigate(action)
//        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.addTask() // Execute your update logic here
                    // Remove this line if you don't want the default back navigation
                    findNavController().navigateUp() // Navigate up
                }
            })


        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}