package project.c323.bonusproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import project.c323.bonusproject.databinding.FragmentEditTaskBinding


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

        // navigates back to taskFragment on data change.
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editTaskFragment_to_tasksFragment)
                viewModel.onNavigatedToList()
            }
        })
        return view
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}