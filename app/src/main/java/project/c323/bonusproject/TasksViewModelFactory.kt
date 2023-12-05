package project.c323.bonusproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * factory for generating TasksViewModels
 */
class TasksViewModelFactory(private val dao: TaskDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
    /*override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }*/
}