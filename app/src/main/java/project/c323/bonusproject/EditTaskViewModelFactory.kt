package project.c323.bonusproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditTaskViewModelFactory(private val taskId: Long,
                               private val dao: TaskDao)
    : ViewModelProvider.Factory {
    /**
     * Factory that allows for generation of viewModel
     */

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            return EditTaskViewModel(taskId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}