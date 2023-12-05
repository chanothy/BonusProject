package project.c323.bonusproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfirmDeleteDialogFragment(val taskId : Long,val clickListener: (taskId: Long) -> Unit) : DialogFragment() {
    /**
     * Delete Dialog.
     *
     * Pop up confirmation for deleting a note item in the UI.
     * Allows for yes and no options. Yes will delete the note item using the dao.
     *
     * @property taskID - ID of item to be deleted
     * @property clickListener - listens for clicks
     *
     */
    val TAG = "ConfirmDeleteDialogFragment"
    interface myClickListener {
        fun yesPressed()
    }



    var listener: myClickListener? = null

    /**
     * Creates the dialog box for the yes no box.
     * Also contains the viewmodel and dao information allowing the viewmodel to delete
     * a specific item marked with taskID.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _,_ -> clickListener(taskId)
                val application = requireNotNull(this.activity).application
                val dao = TaskDatabase.getInstance(application).taskDao
                val viewModelFactory = TasksViewModelFactory(dao)
                val viewModel = ViewModelProvider(this, viewModelFactory)[TasksViewModel::class.java]
                viewModel.deleteNote(taskId)

            }
            .setNegativeButton(getString(R.string.no)) { _,_ -> }

            .create()

    companion object {
        const val TAG = "ConfirmDeleteDialogFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as myClickListener
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

}