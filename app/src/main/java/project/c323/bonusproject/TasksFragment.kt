package project.c323.bonusproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import project.c323.bonusproject.databinding.FragmentTasksBinding


/**
 * A simple [Fragment] subclass.
 * Use the [TasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TasksFragment : Fragment()   {
    /**
     * Serves as home screen
     *
     * Has a recycler view that shows all the note items. Also allows for creation of new notes.
     */
    val TAG = "TasksFragment"
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(TasksViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        fun taskClicked (taskId : Long) {
            viewModel.onTaskClicked(taskId)
        }
        fun yesPressed(taskId : Long) {
            Log.d(TAG, "in yesPressed(): taskId = $taskId")
        }
        fun deleteClicked (taskId : Long) {
             ConfirmDeleteDialogFragment(taskId,::yesPressed).show(childFragmentManager, ConfirmDeleteDialogFragment.TAG)
        }
        val adapter = TaskItemAdapter(::taskClicked,::deleteClicked)

        binding.tasksList.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer { taskId ->
            taskId?.let {
                val action = TasksFragmentDirections
                    .actionTasksFragmentToEditTaskFragment(taskId)
                this.findNavController().navigate(action)
                viewModel.onTaskNavigated()
            }
        })

        val toolbar: MaterialToolbar = view.findViewById(R.id.materialToolbar)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)


        return view
    }

    // send email process
    private val sendEmail =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Feedback","sent")
            } else {
                Log.d("Feedback","failed")
            }
        }

    // send email and set up texts
    private fun sendFeedback() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("timchan@iu.edu"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for IMDBish")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter feedback here")

        val chooser = Intent.createChooser(emailIntent, "Send email using...")
        sendEmail.launch(chooser)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    private fun aboutPage() {
        val url = "https://luddy.indiana.edu/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val viewModel : TasksViewModel by activityViewModels()

        when (item.itemId) {
            R.id.feedback -> {
                sendFeedback()
                return true
            }
            R.id.add -> {
                val action = TasksFragmentDirections.actionTasksFragmentToNoteFragment()
                this.findNavController().navigate(action)
                Log.d("add","button clicked")
                return true
            }
            R.id.about -> {
                aboutPage()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}